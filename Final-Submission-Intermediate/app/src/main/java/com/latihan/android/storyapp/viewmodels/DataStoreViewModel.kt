package com.latihan.android.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.latihan.android.storyapp.api.User
import com.latihan.android.storyapp.preference.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(val preferences: UserPreferences): ViewModel() {
    fun getUser(): LiveData<User> {
        return preferences.getUser().asLiveData()
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            preferences.saveUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferences.logout()
        }
    }
}