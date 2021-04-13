package kr.butterknife.talenthouse

import android.content.Context

private const val SPF_NAME = "ButterKnife"
private const val LOGIN_KEY = "LoginInfo"

object LoginInfo {
    fun setLoginInfo(id: Int, context: Context) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit();
        editor.putInt(LOGIN_KEY, id)
        editor.commit()
    }

    fun getLoginInfo(context: Context): Int {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val ret = spf.getInt(LOGIN_KEY, 0)

        return ret
    }

    fun logout(context : Context) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit()

        editor.remove(LOGIN_KEY)
        editor.commit()
    }
}