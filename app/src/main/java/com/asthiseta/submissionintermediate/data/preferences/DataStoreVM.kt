package com.asthiseta.submissionintermediate.data.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.asthiseta.submissionintermediate.data.model.auth.UsrSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreVM @Inject constructor(private val pref: UserLoginPreferences) : ViewModel(){

    fun getLoginSession(): LiveData<UsrSession>{
        return pref.getLoginData().asLiveData()
    }

    fun setLoginSession(loggedUser: UsrSession){

        viewModelScope.launch {
            pref.setUsrLogin(loggedUser)
        }
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }
}