package com.latihan.android.storyapp.api

data class User(
    val name: String,
    val token: String,
    val userId: String,
    val isLogin: Boolean
)