package com.lolos.asn.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val Token = stringPreferencesKey("user")

    fun getUser(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[Token]
        }
    }

    suspend fun saveUser(token: String) {
        dataStore.edit { preferences ->
            preferences[Token] = token
        }
    }

    suspend fun destroyUser() {
        dataStore.edit { preferences ->
            preferences[Token] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}