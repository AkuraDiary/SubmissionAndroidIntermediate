package com.asthiseta.submissionintermediate.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResponse
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResult
import com.asthiseta.submissionintermediate.data.model.auth.UserRegisterResponse
import com.asthiseta.submissionintermediate.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(private val repos: Repository): ViewModel(){

    val usrLogin: LiveData<UserLoginResult> = repos.userLogin


    val message: LiveData<String> = repos.message


    val isLoading: LiveData<Boolean> = repos.isLoading

    fun doLogin(email:String, password:String) =
        repos.doLoginUser(email, password)

    fun doRegister(name: String, email: String, password: String) =
        repos.doRegisterUser(name, email, password)
}