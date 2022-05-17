package com.latihan.android.storyapp.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.latihan.android.storyapp.api.ListStoryItem
import com.latihan.android.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val storyRepository: StoryRepository) :
    ViewModel() {
    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    fun getStory(): LiveData<PagingData<ListStoryItem>>{
        return storyRepository.showStoryList().cachedIn(viewModelScope).asLiveData()
    }
}