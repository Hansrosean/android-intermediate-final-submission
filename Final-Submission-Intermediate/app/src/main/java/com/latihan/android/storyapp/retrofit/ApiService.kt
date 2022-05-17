package com.latihan.android.storyapp.retrofit

import com.latihan.android.storyapp.api.CreateStoryResponse
import com.latihan.android.storyapp.api.LoginResponse
import com.latihan.android.storyapp.api.RegisterResponse
import com.latihan.android.storyapp.api.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // register
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>

    // login
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password")password: String
    ): Call<LoginResponse>

    // get story data
    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page")page: Int,
        @Query("size")size: Int
    ): StoryResponse

    // get story data & story location
    @GET("stories")
    fun getStoryLocation(
        @Header("Authorization") token: String,
        @Query("location")location: Int
    ): Call<StoryResponse>

    // create story
    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<CreateStoryResponse>
}