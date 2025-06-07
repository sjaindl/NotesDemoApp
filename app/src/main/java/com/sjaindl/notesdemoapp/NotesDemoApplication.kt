package com.sjaindl.notesdemoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotesDemoApplication: Application() {
    companion object {
       var isLoggedIn = false
    }
}
