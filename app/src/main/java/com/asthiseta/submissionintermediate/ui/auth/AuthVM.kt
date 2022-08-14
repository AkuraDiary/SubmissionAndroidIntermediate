package com.asthiseta.submissionintermediate.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResponse
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResult
import com.asthiseta.submissionintermediate.data.model.auth.UserRegisterResponse
import com.asthiseta.submissionintermediate.data.remote.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthVM : ViewModel(){
    private val _usrLogin = MutableLiveData<UserLoginResult>()
    val usrLogin: LiveData<UserLoginResult> = _usrLogin

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun doLogin(email:String, password:String){
        _isLoading.value = true
        RetrofitConfig.getApiService().userLogin(email, password)
            .enqueue(object : Callback<UserLoginResponse> {
                override fun onResponse(
                    call: Call<UserLoginResponse>,
                    response: Response<UserLoginResponse>
                ) {
                    _isLoading.value = false
                    if (!response.isSuccessful){
                        _message.value = response.message()
                    }
                    _usrLogin.value = response.body()?.loginResult
                    _message.value = response.body()?.message
                }

                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                    _message.value = t.message
                    _isLoading.value = false
                }

            })
    }

    fun doRegister(name: String, email: String, password: String){
        _isLoading.value = true
        RetrofitConfig.getApiService().userRegister(name,email,password)
            .enqueue(object : Callback<UserRegisterResponse>{
                override fun onResponse(
                    call: Call<UserRegisterResponse>,
                    response: Response<UserRegisterResponse>
                ) {
                    _isLoading.value = false
                    if (!response.isSuccessful){
                        _message.value = response.message()
                    }
                    _message.value = response.body()?.message
                }

                override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                    _message.value = t.message
                    _isLoading.value = false
                }

            })
    }
}