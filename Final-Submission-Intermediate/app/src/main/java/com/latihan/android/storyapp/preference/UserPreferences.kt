package com.latihan.android.storyapp.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.latihan.android.storyapp.api.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences @Inject constructor(@ApplicationContext val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
        private val USER_ID = stringPreferencesKey("password")
        private val STATE = booleanPreferencesKey("state")
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[NAME] = user.name
            preferences[TOKEN] = user.token
            preferences[USER_ID] = user.userId
            preferences[STATE] = user.isLogin
        }
    }
    
    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[NAME] ?:"",
                preferences[TOKEN] ?:"",
                preferences[USER_ID] ?:"",
                preferences[STATE] ?: false
            )
        }
    }
    
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[NAME] = ""
            preferences[TOKEN] = ""
            preferences[USER_ID] = ""
            preferences[STATE] = false
            preferences.clear()
        }
    }
}