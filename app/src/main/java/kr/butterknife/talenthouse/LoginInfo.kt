package kr.butterknife.talenthouse

import android.content.Context

private const val SPF_NAME = "ButterKnife"
private const val LOGIN_KEY = "LoginInfo"

object LoginInfo {
    fun setLoginInfo(id: String, context: Context) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString(LOGIN_KEY, id)
        editor.commit()
    }

    fun getLoginInfo(context: Context): String {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val ret = spf.getString(LOGIN_KEY, "")

        return ret ?: ""
    }

    fun logout(context : Context) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit()

        editor.remove(LOGIN_KEY)
        editor.commit()
    }
}