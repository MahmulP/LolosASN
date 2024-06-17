package com.lolos.asn.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lolos.asn.data.data.UserData
import com.lolos.asn.data.response.UserDataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val Token = stringPreferencesKey("token")
    private val Username = stringPreferencesKey("username")
    private val UserId = stringPreferencesKey("userid")
    private val Avatar = stringPreferencesKey("avatar")
    private val Role = stringPreferencesKey("role")
    private val Email = stringPreferencesKey("email")
    private val Phone = stringPreferencesKey("phone")

    fun getAuthUser(): Flow<UserData> {
        return dataStore.data.map { preferences ->
            UserData(
                token = preferences[Token],
                userId = preferences[UserId]
            )
        }
    }

    suspend fun saveUser(token: String, userId: String) {
        dataStore.edit { preferences ->
            preferences[Token] = token
            preferences[UserId] = userId
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getUserData(): Flow<UserDataResponse> {
        return dataStore.data.map { preferences ->
            UserDataResponse(
                name = preferences[Username],
                email = preferences[Email],
                role = preferences[Role],
                phone =  preferences[Phone],
                avatar = preferences[Avatar]
            )
        }
    }

    suspend fun saveUserData(username: String, email: String, role: String, avatar: String?, phone: String) {
        dataStore.edit { preferences ->
            preferences[Username] = username
            preferences[Email] = email
            preferences[Role] = role
            preferences[Avatar] = avatar ?: "avatar"
            preferences[Phone] = phone
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
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