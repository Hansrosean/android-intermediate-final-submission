package com.latihan.android.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.latihan.android.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateStoryViewModel @Inject constructor(val storyRepository: StoryRepository): ViewModel() {
    val message: LiveData<String> = storyRepository.message
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    
    fun createStory(token: String, image: File, description: String) {
        storyRepository.createStory(token, image, description)
    }
}