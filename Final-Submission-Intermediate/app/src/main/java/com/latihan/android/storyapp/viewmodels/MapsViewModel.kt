package com.latihan.android.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.latihan.android.storyapp.api.ListStoryItem
import com.latihan.android.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(val storyRepository: StoryRepository) : ViewModel() {
    val listListStoryItem: LiveData<List<ListStoryItem>> = storyRepository.listStory

    fun getStoryWithLocation(token: String) {
        storyRepository.showStoryInMaps("Bearer $token")
    }
}