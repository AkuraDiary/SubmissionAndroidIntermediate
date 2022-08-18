package com.asthiseta.submissionintermediate.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.asthiseta.submissionintermediate.data.local.room.StoryDatabase
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResponse
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResult
import com.asthiseta.submissionintermediate.data.model.auth.UserRegisterResponse
import com.asthiseta.submissionintermediate.data.model.stories.AddStoryResponse
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.data.model.stories.StoryListResponse
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.data.remote.ApiService
import com.asthiseta.submissionintermediate.paging.StoryRemoteMediator
import com.asthiseta.submissionintermediate.utilities.wrapWithEspressoIdlingResource
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class Repository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val service: ApiService,
    private val pref: UserLoginPreferences
) {
    private val _userLogin = MutableLiveData<UserLoginResult>()
    val userLogin: LiveData<UserLoginResult> = _userLogin

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _storyList = MutableLiveData<List<Story>>()
    val storyList: LiveData<List<Story>> = _storyList


    @OptIn(ExperimentalPagingApi::class)
    fun getStoriesData(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, service, pref),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoriesWithLocation(token: String) {
        wrapWithEspressoIdlingResource {
            service.getAllStoryWithLocation(token, 1)
                .enqueue(object : Callback<StoryListResponse> {
                    override fun onResponse(
                        call: Call<StoryListResponse>,
                        response: Response<StoryListResponse>
                    ) {
                        if (response.isSuccessful) {
                            _storyList.postValue(response.body()?.listStory)
                            _message.postValue(response.body()?.message)
                        } else {
                            _message.postValue(response.message())
                        }
                    }

                    override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                        _message.postValue(t.message)
                    }
                })
        }
    }

    fun uploadStory(token: String, picture: File, desc: String) {
        wrapWithEspressoIdlingResource {
            val description = desc.toRequestBody("text/plain".toMediaType())
            val pictureFile = picture.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                picture.name,
                pictureFile
            )
            val service = service.uploadStory(token, imageMultipart, description)
            service.enqueue(object : Callback<AddStoryResponse> {
                override fun onResponse(
                    call: Call<AddStoryResponse>,
                    response: Response<AddStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            _message.value = responseBody.message
                        } else {
                            _message.value = response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                    _message.value = t.message
                }
            })
        }
    }

    fun uploadStoryWithLocation(
        token: String,
        imageMultipartBody: MultipartBody.Part,
        storyDesc: RequestBody,
        usrLat: Float,
        usrLong: Float
    ) {
        service.uploadStoryWithLocation(
            token,
            imageMultipartBody,
            storyDesc,
            usrLat,
            usrLong
        ).enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _message.value = responseBody.message
                    } else {
                        _message.value = response.message()
                    }
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _message.value = t.message
            }

        })
    }

    fun  doLoginUser(usrEmail: String, usrPass: String){
        wrapWithEspressoIdlingResource {
            _isLoading.value  = false
            service.userLogin(usrEmail, usrPass)
                .enqueue(object : Callback<UserLoginResponse>{
                    override fun onResponse(
                        call: Call<UserLoginResponse>,
                        response: Response<UserLoginResponse>
                    ) {
                        if(response.isSuccessful){
                            _message.value =response.body()?.message
                            _userLogin.value = response.body()?.loginResult
                        }else{
                            _message.value = response.message()
                        }

                    }

                    override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                        _message.value = t.message
                    }

                })
            _isLoading.value  = false
        }
    }

    fun doRegisterUser(usrName: String, usrEmail:String, usrPass:String){
        wrapWithEspressoIdlingResource {
            _isLoading.value = true
            service.userRegister(usrName, usrEmail, usrPass)
                .enqueue(object : Callback<UserRegisterResponse>{
                    override fun onResponse(
                        call: Call<UserRegisterResponse>,
                        response: Response<UserRegisterResponse>
                    ) {
                        if(response.isSuccessful){
                            _message.value = response.message()
                        }
                    }

                    override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                        _message.value = t.message
                    }

                })
            _isLoading.value = false

        }
    }

}