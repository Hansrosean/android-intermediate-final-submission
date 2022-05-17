package com.latihan.android.storyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.latihan.android.storyapp.api.ListStoryItem
import com.latihan.android.storyapp.paging.RemoteKeys

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase(){

    abstract fun storyDao(): StoryDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}