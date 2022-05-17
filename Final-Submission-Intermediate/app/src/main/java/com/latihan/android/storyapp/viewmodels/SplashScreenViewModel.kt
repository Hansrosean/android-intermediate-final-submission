package com.latihan.android.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.latihan.android.storyapp.api.User
import com.latihan.android.storyapp.preference.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(val preferences: UserPreferences) : ViewModel() {

    fun getUser(): LiveData<User> {
        return preferences.getUser().asLiveData()
    }
}