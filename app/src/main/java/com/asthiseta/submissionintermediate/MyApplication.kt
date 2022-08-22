package com.asthiseta.submissionintermediate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(){
    companion object{
        var BASE_URL = BuildConfig.BASE_URL
    }
}