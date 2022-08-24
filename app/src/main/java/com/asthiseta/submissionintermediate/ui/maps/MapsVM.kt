package com.asthiseta.submissionintermediate.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsVM @Inject constructor(private val repos: Repository) : ViewModel(){

    val storyList : LiveData<List<Story>> = repos.storyList

    fun getStoriesWithLocations(usrToken: String){
        repos.getStoriesWithLocation("Bearer $usrToken")
    }
}