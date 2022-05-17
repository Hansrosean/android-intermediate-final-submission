package com.latihan.android.storyapp.retrofit

import android.content.Context
import androidx.room.Room
import com.latihan.android.storyapp.BuildConfig
import com.latihan.android.storyapp.db.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiConfig {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : StoryDatabase {
        return Room.databaseBuilder(
            context,
            StoryDatabase::class.java,
            "StoryDB"
        ).build()
    }

    @Provides
    fun providesStoryDao(database: StoryDatabase) = database.storyDao()

    @Provides
    fun getApiService(): Retrofit {
        val loggingInterceptor = if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
        return apiService
    }
}