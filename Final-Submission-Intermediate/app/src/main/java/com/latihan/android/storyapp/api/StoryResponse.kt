package com.latihan.android.storyapp.api

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @field:SerializedName("listStory")
    val listStoryItem: List<ListStoryItem>,
    
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)