package com.asthiseta.submissionintermediate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.data.model.stories.StoryListResponse
import com.asthiseta.submissionintermediate.data.remote.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listStoryData = MutableLiveData<ArrayList<Story>>()
    val listStoryData : LiveData<ArrayList<Story>> = _listStoryData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()



    fun getAllStoriesData(auth: String){
        _isLoading.value = true
        RetrofitConfig.getApiService().getAllStory("Bearer $auth")
            .enqueue(object : Callback<StoryListResponse>{
                override fun onResponse(
                    call: Call<StoryListResponse>,
                    response: Response<StoryListResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _listStoryData.postValue(response.body()?.listStory)
                        _message.postValue(response.body()?.message)
                    }else{
                        _message.postValue(response.message())
                    }


                }

                override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = t.message
                }

            })
    }
}