package com.latihan.android.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.latihan.android.storyapp.api.LoginResult
import com.latihan.android.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val storyRepository: StoryRepository) : ViewModel() {
    val login: LiveData<LoginResult> = storyRepository.loginResult
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message
    
    fun loginData(email: String, password: String) = storyRepository.dataLogin(email, password)
}