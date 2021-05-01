package kr.butterknife.talenthouse

import android.content.Context

private const val SPF_NAME = "ButterKnife"
private const val LOGIN_ID = "LoginInfoId"
private const val LOGIN_NAME = "LoginInfoName"

object LoginInfo {
    fun setLoginInfo(id: String, nickname : String, context: Context) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString(LOGIN_ID, id)
        editor.putString(LOGIN_NAME, nickname)
        editor.commit()
    }

    fun getLoginInfo(context: Context): Array<String> {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val id = spf.getString(LOGIN_ID, "")!!
        val nickname = spf.getString(LOGIN_NAME, "")!!

        return arrayOf(id, nickname)
    }

    fun logout(context : Context) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit()

        editor.remove(LOGIN_ID)
        editor.remove(LOGIN_NAME)
        editor.commit()
    }
}