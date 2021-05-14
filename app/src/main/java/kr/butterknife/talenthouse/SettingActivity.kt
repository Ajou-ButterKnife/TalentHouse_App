package kr.butterknife.talenthouse

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.butterknife.talenthouse.network.ButterKnifeApi
import kr.butterknife.talenthouse.network.request.PWUpdateReq
import kr.butterknife.talenthouse.network.request.ProfileUpdateReq
import kr.butterknife.talenthouse.network.request.UserInfoUpdateReq
import kr.butterknife.talenthouse.network.response.CommonResponse
import kr.butterknife.talenthouse.network.response.UserInfo
import kr.butterknife.talenthouse.network.response.UserInfoRes
import java.io.File

class SettingActivity : AppCompatActivity() {
    private lateinit var items : MutableList<SettingItem>
    private lateinit var rvAdapter : SettingRVAdapter
    private lateinit var coroutineScope : CoroutineScope
    private var userInfoRes : UserInfoRes? = null
    private var userInfo : UserInfo? = null
    private var response : CommonResponse? = null
    private lateinit var loginInfo : Array<String>
    private val PROFILE_REQUEST_CODE = 605
    private var profileImgFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        coroutineScope = CoroutineScope(Dispatchers.Main + Job())

        loginInfo = LoginInfo.getLoginInfo(applicationContext)

        items = mutableListOf()
        rvAdapter = SettingRVAdapter(applicationContext, items)
        setting_rv.adapter = rvAdapter

        getUserInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            PROFILE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    try {
                        val inputStream = contentResolver.openInputStream(data?.data!!)
                        val img = BitmapFactory.decodeStream(inputStream)
                        inputStream?.close()
                        setImage(img)
                        val uri = data.clipData?.getItemAt(0)?.uri
                        profileImgFile = File(getRealPathFromURI(uri))
                    } catch (e: Exception) {
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(applicationContext, "사진 선택 취소", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getUserInfo() {
        coroutineScope.launch {
            try {
                userInfoRes = ButterKnifeApi.retrofitService.getUserInfo(loginInfo[0])
                userInfoRes?.let { res ->
                    if(res.result == "Success") {
                        userInfo = res.data!!
                        userInfo?.let { data ->
                            rvAdapter.addItem(SettingItem("title", "프로필 이미지"))
                            rvAdapter.addItem(SettingItem("image", "profile", data.profile, onClick = object : OnItemClickListener {
                                override fun onItemClick(v: View?, pos: Int) {
                                    val intent = Intent()
                                    intent.type = "image/*"
                                    intent.action = Intent.ACTION_PICK;
                                    startActivityForResult(intent, PROFILE_REQUEST_CODE)
                                }
                            }))
                            rvAdapter.addItem(SettingItem("button", "프로필 저장", onClick = object : OnItemClickListener {
                                override fun onItemClick(v: View?, pos: Int) {
                                    coroutineScope.launch {
                                        uploadProfileInS3()
                                        updateProfile()
                                    }
                                }
                            }))

                            rvAdapter.addItem(SettingItem("title", "개인 정보 수정"))
                            rvAdapter.addItem(SettingItem("text", "phone", data.phone))
                            rvAdapter.addItem(SettingItem("text", "nickname", data.nickname))
                            rvAdapter.addItem(SettingItem("spinner", "category", listValue = data.category))
                            rvAdapter.addItem(SettingItem("button", "저장하기", onClick = object : OnItemClickListener {
                                override fun onItemClick(v: View?, pos: Int) {
                                    updateInfo()
                                }
                            }))

                            rvAdapter.addItem(SettingItem("title", "비밀번호 변경"))
                            rvAdapter.addItem(SettingItem("text", "password", ""))
                            rvAdapter.addItem(SettingItem("button", "비밀번호 변경하기", onClick = object : OnItemClickListener {
                                override fun onItemClick(v: View?, pos: Int) {
                                    updatePassword()
                                }
                            }))

                            rvAdapter.addItem(SettingItem("title", "알림 설정"))
                            rvAdapter.addItem(SettingItem("switch", "", strValue = "알림"))

                            setting_is_social.visibility = if (data.isSocial) View.VISIBLE else View.GONE
                        }
                    }
                    else {
                        Toast.makeText(applicationContext, res.detail, Toast.LENGTH_SHORT).show()
                        // alert dialog 뛰어주거나 하자.
                    }
                }
            }
            catch (e: Exception) {}
        }
    }

    private fun updateInfo() {
        val req = UserInfoUpdateReq("", "", listOf())
        setting_rv.apply {
            for(i in 0 until rvAdapter.itemCount) {
                if(items[i].type == "text") {
                    when(items[i].name) {
                        "phone" -> {
                            req.phone = (this.findViewHolderForAdapterPosition(i) as SettingTextVH).et.text.toString()
                        }
                        "nickname" -> {
                            req.nickname = (this.findViewHolderForAdapterPosition(i) as SettingTextVH).et.text.toString()
                        }
                    }
                }
                else if(items[i].type == "spinner")
                    if(items[i].name == "category") {
                        val chipgroup = (this.findViewHolderForAdapterPosition(i) as SettingSpinnerVH).chipGroup
                        val list = mutableListOf<String>()
                        for(j in 0 until chipgroup.childCount) {
                            list.add((chipgroup.getChildAt(j) as Chip).text.toString())
                        }
                        req.category = list.toList()
                    }
            }
        }
        LoginInfo.setLoginInfo(loginInfo[0], req.nickname, applicationContext)
        loginInfo = LoginInfo.getLoginInfo(applicationContext)
        coroutineScope.launch {
            try {
                response = ButterKnifeApi.retrofitService.updateInfo(loginInfo[0], req)
                response?.let {
                    if (it.result == "Success") {
                        Toast.makeText(applicationContext, "개인 정보 변경 완료", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, it.detail
                            ?: "알 수 없는 오류", Toast.LENGTH_SHORT).show()
                    }
                }
                clearAll()
            }
            catch (e: Exception) {}
        }
    }

    private fun updatePassword() {
        var pw = ""
        setting_rv.apply {
            for(i in 0 until rvAdapter.itemCount)
                if(items[i].type == "text" && items[i].name == "password") {
                    pw = (this.findViewHolderForAdapterPosition(i) as SettingTextVH).et.text.toString()
                    Toast.makeText(applicationContext, pw, Toast.LENGTH_SHORT).show()
                    break
                }
        }
        if(pw == "") {
            Toast.makeText(applicationContext, "변경할 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        coroutineScope.launch {
            try {
                response = ButterKnifeApi.retrofitService.updatePassword(loginInfo[0], PWUpdateReq(pw))
                response?.let {
                    if(it.result == "Success") {
                        Toast.makeText(applicationContext, "비밀번호 변경 완료", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(applicationContext, it.detail
                            ?: "알 수 없는 오류", Toast.LENGTH_SHORT).show()
                    }
                }
                clearAll()
            }
            catch (e: Exception) {}
        }
    }

    private fun setImage(image: Bitmap) {
        setting_rv.apply {
            for(i in 0 until rvAdapter.itemCount)
                if(items[i].type == "image" && items[i].name == "profile") {
                    val imgView = (this.findViewHolderForAdapterPosition(i) as SettingImageVH).profileImage
                    Glide.with(applicationContext)
                        .load(image)
                        .into(imgView)
                    break
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = this.contentResolver.query(contentUri!!, proj, null, null)
        cursor?.let {
            cursor.moveToNext()
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
            cursor.close()
            return path
        }
        return null
    }

    fun uploadProfileInS3() {
        profileImgFile?.let {
            // CredentialsProvider 객체 생성 (Cognito에서 자격 증명 풀 ID 제공)
            val awsCredentials: AWSCredentials = BasicAWSCredentials("AKIAQUQIUUPFLLOMLQUD", "Yi6ph/MUh6ISRliH2mv0jlwkqtg5hZoJ5LjQU4Ia")
            val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

            val transferUtility = TransferUtility.builder().s3Client(s3Client).context(applicationContext).build()
            TransferNetworkLossHandler.getInstance(applicationContext)

            val uploadObserver = transferUtility.upload("talent-house-app/photo", loginInfo[0] + it.name, it)
            uploadObserver.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    if (state == TransferState.COMPLETED) {
                        // Handle a completed upload
                    }
                }

                override fun onProgressChanged(id: Int, current: Long, total: Long) {
                    val done = (current.toDouble() / total * 100.0).toInt()
                    Log.d("MYTAG", "UPLOAD - - ID: \$id, percent done = \$done")
                }

                override fun onError(id: Int, ex: java.lang.Exception) {
                    Log.d("MYTAG", "UPLOAD ERROR - - ID: \$id - - EX:$ex")
                }
            })

            // If you prefer to long-poll for updates
            if (uploadObserver.state == TransferState.COMPLETED) {
                /* Handle completion */
            }
        }
    }

    fun updateProfile() {
        if(profileImgFile != null) {
            val imgUrl = "https://talent-house-app.s3.ap-northeast-2.amazonaws.com/photo/" + loginInfo[0] + profileImgFile!!.name
            coroutineScope.launch {
                try {
                    response = ButterKnifeApi.retrofitService.updateProfile(loginInfo[0], ProfileUpdateReq(imgUrl))
                    response?.let {
                        if(it.result == "Success") {
                            Toast.makeText(applicationContext, "프로필 사진 변경 완료", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(applicationContext, it.detail
                                ?: "알 수 없는 오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                    clearAll()
                    profileImgFile = null
                }
                catch (e: Exception) {}
            }
        }
        else {
            Toast.makeText(applicationContext, "프로필 사진을 새로 선택해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    fun clearAll() {
        rvAdapter.clearItem(setting_rv)
        getUserInfo()
        response = null
    }
}