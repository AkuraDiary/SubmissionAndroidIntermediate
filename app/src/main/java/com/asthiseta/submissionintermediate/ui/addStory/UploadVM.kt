package com.asthiseta.submissionintermediate.ui.addStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.asthiseta.submissionintermediate.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadVM @Inject constructor(private val repos : Repository): ViewModel() {
    val message : LiveData<String> = repos.message
    fun uploadStory(token: String, image : File, desc:String){
        repos.uploadStory(token, image, desc)
    }

    fun uploadStoryWithLocation(token: String, file: File, descriptionText: String, _latitude: Double, _longitude: Double) {
        repos.uploadStoryWithLocation(token, file, descriptionText, _latitude.toFloat(), _longitude.toFloat())
    }
}