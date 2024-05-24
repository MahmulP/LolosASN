package com.lolos.asn.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.introPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "intro")

class IntroductionPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val introKey = booleanPreferencesKey("intro")

    fun getIntroStatus(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[introKey]
        }
    }

    suspend fun saveStatus(isIntro: Boolean) {
        dataStore.edit { preferences ->
            preferences[introKey] = isIntro
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: IntroductionPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): IntroductionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = IntroductionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}