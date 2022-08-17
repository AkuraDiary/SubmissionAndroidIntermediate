package com.asthiseta.submissionintermediate.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.asthiseta.submissionintermediate.BuildConfig.PREF_NAME
import com.asthiseta.submissionintermediate.data.model.auth.UsrSession

class UserLoginPreferences(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun setUsrLogin(user: UsrSession){
        editor.apply{
            putString(NAME_KEY, user.name)
            putString(TOKEN_KEY, user.token)
            putString(USER_ID_KEY, user.userId)
            putBoolean(STATE_KEY, user.isLogin)
            apply()
        }
    }

    fun logout(){
        editor.apply {
            remove(NAME_KEY)
            remove(TOKEN_KEY)
            remove(USER_ID_KEY)
            putBoolean(STATE_KEY, false)
            apply()
        }
    }

    fun getLoginData(): UsrSession{
        val logedUser = UsrSession(
            pref.getString(NAME_KEY, "").toString(),
            pref.getString(TOKEN_KEY, "").toString(),
            pref.getString(USER_ID_KEY, "").toString(),
            pref.getBoolean(STATE_KEY, false)
        )
        Log.d("UserLoginPreferences", "getLoginData: $logedUser")
        return logedUser
    }

    companion object{
        private const val NAME_KEY = "NAME"
        private const val TOKEN_KEY = "TOKEN"
        private const val USER_ID_KEY = "USER_ID"
        private const val STATE_KEY = "STATE"


    }
}