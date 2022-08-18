package com.asthiseta.submissionintermediate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(repos : Repository) : ViewModel() {

    val isLoading: LiveData<Boolean> = repos.isLoading
    val message : LiveData<String> = repos.message
    val stories: LiveData<PagingData<Story>> = repos.getStoriesData().cachedIn(viewModelScope)

}