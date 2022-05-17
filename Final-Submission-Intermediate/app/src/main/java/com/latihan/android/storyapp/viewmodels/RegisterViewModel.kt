package com.latihan.android.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.latihan.android.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val storyRepository: StoryRepository) : ViewModel() {
    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    val message: LiveData<String> = storyRepository.message

    fun saveDataRegister(name: String, email: String, password: String) {
        storyRepository.dataRegister(name, email, password)
    }
}