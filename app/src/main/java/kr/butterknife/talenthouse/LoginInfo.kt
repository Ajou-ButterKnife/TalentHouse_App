package kr.butterknife.talenthouse

import android.content.Context

private const val SPF_NAME = "ButterKnife"
private const val LOGIN_ID = "LoginInfoId"
private const val LOGIN_NAME = "LoginInfoName"
private const val LOGIN_PROFILE = "LoginInfoProfile"
private const val NOTIFICATION = "Noti-talenthouse"

object LoginInfo {
    fun setLoginInfo(id: String, nickname : String, profile : String?, context: Context) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString(LOGIN_ID, id)
        editor.putString(LOGIN_NAME, nickname)
        editor.putString(LOGIN_PROFILE, profile)
        editor.apply()
    }

    fun getLoginInfo(context: Context): Array<String> {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val id = spf.getString(LOGIN_ID, "")!!
        val nickname = spf.getString(LOGIN_NAME, "")!!
        val profile = spf.getString(LOGIN_PROFILE, "")!!

        return arrayOf(id, nickname, profile)
    }

    fun logout(context : Context) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit()

        editor.remove(LOGIN_ID)
        editor.remove(LOGIN_NAME)
        editor.remove(LOGIN_PROFILE)
        editor.apply()
    }

    fun setNotiPermission(context : Context, cur : Boolean) {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)
        val editor = spf.edit()
        editor.putBoolean(NOTIFICATION, cur)
        editor.apply()
    }

    fun getNotiPermission(context : Context) : Boolean {
        val spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE)

        return spf.getBoolean(NOTIFICATION, true)
    }
}