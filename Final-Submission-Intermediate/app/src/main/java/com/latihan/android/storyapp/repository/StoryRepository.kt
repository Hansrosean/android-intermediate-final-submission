package com.latihan.android.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.latihan.android.storyapp.api.*
import com.latihan.android.storyapp.db.StoryDatabase
import com.latihan.android.storyapp.helper.wrapEspressoIdlingResource
import com.latihan.android.storyapp.paging.StoryRemoteMediator
import com.latihan.android.storyapp.preference.UserPreferences
import com.latihan.android.storyapp.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val service: ApiService,
    private val preferences: UserPreferences
) {

    private val loginresult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = loginresult

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    companion object {
        private const val CONNECT = "connect"
    }

    @OptIn(ExperimentalPagingApi::class)
    fun showStoryList(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),

            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                service,
                preferences
            ),

            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).flow
    }

    fun showStoryInMaps(token: String) {
        wrapEspressoIdlingResource {
            service.getStoryLocation(token, 1)
                .enqueue(object : Callback<StoryResponse> {
                    override fun onResponse(
                        call: Call<StoryResponse>,
                        response: Response<StoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            _listStory.postValue(response.body()?.listStoryItem)
                        }
                    }

                    override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                        Log.d(CONNECT, t.message.toString())
                    }
                })
        }
    }

    fun dataRegister(name: String, email: String, password: String) {
        wrapEspressoIdlingResource {
            _isLoading.value = true
            service.register(name, email, password)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            _message.value = response.body()?.message
                            Log.d(CONNECT, response.body()?.message.toString())
                        }

                        if (!response.isSuccessful) {
                            _message.value = response.message()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        _message.value = t.message
                        _isLoading.value = false
                        Log.d(CONNECT, t.message.toString())
                    }
                })
        }
    }

    fun dataLogin(email: String, password: String) {
        wrapEspressoIdlingResource {
            _isLoading.value = true
            service.login(email, password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            _message.value = response.body()?.message
                            loginresult.value = response.body()?.loginResult

                            Log.d(CONNECT, response.body()?.message.toString())
                            Log.d(CONNECT, response.body()?.loginResult?.token.toString())
                        }
                        if (!response.isSuccessful) {
                            _message.value = response.message()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        _message.value = t.message
                        _isLoading.value = false

                        Log.d(CONNECT, t.message.toString())
                    }
                })
        }
    }

    fun createStory(token: String, image: File, description: String) {
        wrapEspressoIdlingResource {

            val storyDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImage = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                image.name,
                requestImage
            )
            _isLoading.value = true
            val service = service.postStory(token, multipart, storyDescription)
            service.enqueue(object : Callback<CreateStoryResponse> {
                override fun onResponse(
                    call: Call<CreateStoryResponse>,
                    response: Response<CreateStoryResponse>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        Log.e(CONNECT, "onSuccess: ${response.message()}")
                        _message.value = response.body()?.message
                        showStoryList()
                    } else {
                        _message.value = response.message()
                        Log.e(CONNECT, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CreateStoryResponse>, t: Throwable) {
                    Log.d(CONNECT, t.message.toString())
                }
            })
        }
    }
}