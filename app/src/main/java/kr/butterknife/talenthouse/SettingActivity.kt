package kr.butterknife.talenthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.butterknife.talenthouse.network.ButterKnifeApi
import kr.butterknife.talenthouse.network.request.PWUpdateReq
import kr.butterknife.talenthouse.network.request.UserInfoUpdateReq
import kr.butterknife.talenthouse.network.response.CommonResponse
import kr.butterknife.talenthouse.network.response.UserInfo
import kr.butterknife.talenthouse.network.response.UserInfoRes

class SettingActivity : AppCompatActivity() {
    private lateinit var items : MutableList<SettingItem>
    private lateinit var rvAdapter : SettingRVAdapter
    private lateinit var coroutineScope : CoroutineScope
    private var userInfoRes : UserInfoRes? = null
    private var userInfo : UserInfo? = null
    private var response : CommonResponse? = null
    private lateinit var loginInfo : Array<String>

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

    private fun getUserInfo() {
        coroutineScope.launch {
            try {
                userInfoRes = ButterKnifeApi.retrofitService.getUserInfo(loginInfo[0])
                userInfoRes?.let { res ->
                    if(res.result == "Success") {
                        userInfo = res.data!!
                        userInfo?.let { data ->
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
            catch(e : Exception) {}
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
                rvAdapter.clearItem()
                getUserInfo()
                response = null
            }
            catch(e : Exception) {}
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
                        Toast.makeText(applicationContext, it.detail ?: "알 수 없는 오류", Toast.LENGTH_SHORT).show()
                    }
                }
                rvAdapter.clearItem()
                getUserInfo()
                response = null
            }
            catch(e : Exception) {}
        }
    }
}