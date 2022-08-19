package com.asthiseta.submissionintermediate.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.asthiseta.submissionintermediate.BuildConfig.PREF_NAME
import com.asthiseta.submissionintermediate.data.model.auth.UsrSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)

class UserLoginPreferences @Inject constructor(@ApplicationContext val context: Context) {
    private val dataStore = context.datastore

//    private val pref: SharedPreferences =
//        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    suspend fun setUsrLogin(user: UsrSession) {
        dataStore.edit {
            it[USER_ID_KEY] = user.userId
            it[NAME_KEY] = user.name
            it[TOKEN_KEY] = user.token
            it[STATE_KEY] = user.isLogin
        }
    }


    suspend fun logout() {
        dataStore.edit {
            it[USER_ID_KEY] = ""
            it[NAME_KEY] = ""
            it[TOKEN_KEY] = ""
            it[STATE_KEY] = false
        }
    }

    fun getLoginData(): Flow<UsrSession> {
        return dataStore.data.map {
            UsrSession(
                it[USER_ID_KEY].toString(),
                it[NAME_KEY].toString(),
                it[TOKEN_KEY].toString(),
                it[STATE_KEY] ?: false
            )
        }
    }

    companion object {
        private val NAME_KEY = stringPreferencesKey("NAME")
        private val TOKEN_KEY = stringPreferencesKey("TOKEN")
        private val USER_ID_KEY = stringPreferencesKey("USER_ID")
        private val STATE_KEY = booleanPreferencesKey("STATE")

    }
}