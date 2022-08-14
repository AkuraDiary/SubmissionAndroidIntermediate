package com.asthiseta.submissionintermediate.data.preferences

import android.content.Context
import com.asthiseta.submissionintermediate.BuildConfig.PREF_NAME
import com.asthiseta.submissionintermediate.data.model.auth.UsrSession

class UserLoginPreferences(context: Context) {
    val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun setUsrLogin(user: UsrSession){
        editor.apply{
            putString("NAME", user.name)
            putString("TOKEN", user.token)
            putString("USER_ID", user.userId)
            putBoolean("STATE", user.isLogin)
            apply()
        }
    }

    fun logout(){
        editor.apply {
            remove("NAME")
            remove("TOKEN")
            putBoolean("STATE", false)
            apply()
        }
    }

    fun getLoginData(): UsrSession{
        return UsrSession(
            pref.getString("NAME","") ?: "",
            pref.getString("TOKEN","") ?: "",
            pref.getString("USER_ID","") ?: "",
            pref.getBoolean("STATE", false)
        )
    }
}