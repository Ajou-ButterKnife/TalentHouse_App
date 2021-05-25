package kr.butterknife.talenthouse

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.hardware.input.InputManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.butterknife.talenthouse.network.ButterKnifeApi
import kr.butterknife.talenthouse.network.request.FCMTokenRegister
import kr.butterknife.talenthouse.network.request.IdReq
import kr.butterknife.talenthouse.network.response.CommonResponse

object SpinnerUtil {
    fun setCategorySpinner(spinner: Spinner, chipGroup: ChipGroup, context: Context) {
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val str = parent.getItemAtPosition(position).toString()
                if (str == "카테고리" == false) {
                    var alreadySelected = false
                    // 이미 선택되었는지 확인
                    for (i in 0 until chipGroup.getChildCount()) {
                        val category = (chipGroup.getChildAt(i) as Chip).text.toString()
                        if (category == str) {
                            alreadySelected = true
                            Toast.makeText(context, "이미 선택된 카테고리입니다.", Toast.LENGTH_SHORT).show()
                            parent.setSelection(0)
                            break
                        }
                    }
                    if (alreadySelected == false) {
                        // Chip 인스턴스 생성
                        val chip = Chip(view.context)
                        chip.text = str
                        // chip icon 표시 여부
                        chip.isCloseIconVisible = true
                        chip.setBackgroundColor(Color.BLUE)
                        // chip group 에 해당 chip 추가
                        chipGroup.addView(chip)
                        parent.setSelection(0)
                        // chip 인스턴스 클릭 리스너
                        chip.setOnCloseIconClickListener { v -> chipGroup.removeView(v) }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}

object Util {
    private const val SEC = 60
    private const val MIN = 60
    private const val HOUR = 24
    private const val DAY = 30
    private const val MONTH = 12

    fun unixTime2String(regTime: Long): String {
        val curTime = System.currentTimeMillis()
        var diffTime: Long = (curTime - regTime) / 1000
        var msg: String = ""
        when {
            diffTime < SEC ->
                msg = "방금 전"
            SEC.let { diffTime /= it; diffTime } < MIN ->
                msg = diffTime.toString() + "분 전"
            MIN.let { diffTime /= it; diffTime } < HOUR ->
                msg = diffTime.toString() + "시간 전"
            HOUR.let { diffTime /= it; diffTime } < DAY ->
                msg = diffTime.toString() + "일 전"
            DAY.let { diffTime /= it; diffTime } < MONTH ->
                msg = diffTime.toString() + "달 전"
            else -> {
                msg = diffTime.toString() + "년 전"
            }
        }
        return msg
    }

    fun registerFCMToken(context: Context, token: String) {
        val coroutineScope = CoroutineScope(Dispatchers.Default + Job())
        var response: CommonResponse? = null
        coroutineScope.launch {
            try {
                while (true) {
                    response = ButterKnifeApi.retrofitService.registerToken(LoginInfo.getLoginInfo(context)[0], FCMTokenRegister(token))

                    if (response != null && response!!.result == "Success")
                        break
                    response = null
                }
            } catch (e: Exception) {
            }
        }
    }

    fun postSetting(activity : Activity?,
                    context: Context,
                    view: View,
                    postId: String,
                    list: ArrayList<PostItem>,
                    updateAction: (item: PostItem) -> Boolean,
                    deleteAction: (idx: Int) -> Boolean)  {
        val popup = PopupMenu(context, view)
        val menuInflater = popup.menuInflater
        menuInflater.inflate(R.menu.post_menu, popup.menu)
        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.post_menu_update -> {
                    var updateIndex = 0
                    while (updateIndex < list.size) {
                        if (list[updateIndex]._id == postId) break
                        updateIndex++
                    }
                    updateAction(list[updateIndex])
                }
                R.id.post_menu_delete -> {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("게시글 삭제")
                    builder.setMessage("게시글을 삭제하시겠습니까?")
                    builder.setPositiveButton("삭제") { dialog: DialogInterface?, which: Int ->
                        deletePost(activity, postId, context)
                        var deleteIndex = 0
                        while (deleteIndex < list.size) {
                            if (list[deleteIndex]._id == postId) break
                            deleteIndex++
                        }
                        deleteAction(deleteIndex)
                    }
                    builder.setNegativeButton("취소") { dialog: DialogInterface?, which: Int -> Toast.makeText(context, "negative", Toast.LENGTH_SHORT).show() }
                    builder.show()
                }
            }
            true
        }
        popup.show()
    }

    private fun deletePost(activity : Activity?, postId: String, context: Context) {
        val coroutineScope = CoroutineScope(Dispatchers.Default + Job())
        var response: CommonResponse? = null
        coroutineScope.launch {
            try {
                LoadingDialog.onLoadingDialog(activity)
                response = ButterKnifeApi.retrofitService.deletePost(LoginInfo.getLoginInfo(context)[0], IdReq(postId))
                var toastMsg = "서버로 부터 받아오지 못하였습니다."

                response?.let {
                    toastMsg = if(it.result == "Success") "삭제가 완료되었습니다."
                    else it.detail ?: ""
                }

                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                LoadingDialog.offLoadingDialog()
            }
            catch (e: Exception) {
                LoadingDialog.offLoadingDialog()
            }
        }
    }
}

object LoadingDialog {
    var loadingDialog: AppCompatDialog? = null

    fun onLoadingDialog(activity: Activity?) {
        if(activity == null || activity.isFinishing)
            return

        loadingDialog?.let {
            if(it.isShowing)
                return
        }

        loadingDialog = AppCompatDialog(activity).apply {
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_loading)
        }

        loadingDialog?.show()

        val loadingImage = loadingDialog?.findViewById<ImageView>(R.id.loading_img)
        val loadingAni = loadingImage!!.background as AnimationDrawable
        loadingImage.post { loadingAni.start() }
    }

    fun offLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    fun hideKeyboard(context: Context, view: View){
//        val imm = (LayoutInflater) context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputManager
//        imm.hideSo
//        getSystem
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputManager
//        imm.hide
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)


//        InputMethodManager manager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

//        getContext().getSystemService(Context.INPUT_METHOD_SERVICE).hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}

interface OnItemClickListener {
    fun onItemClick(v: View?, pos: Int)
}