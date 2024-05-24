package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.response.LoginRequest
import com.lolos.asn.data.response.LoginResponse
import com.lolos.asn.data.response.RegisterRequest
import com.lolos.asn.data.response.RegisterResponse
import com.lolos.asn.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: UserPreferences): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage = _errorMessage

    fun login(loginRequest: LoginRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(loginRequest)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val token = responseBody.data
                        saveUserToken(token)
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    _errorMessage.value = message
                    Log.e(TAG, "onFailure: $message")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun register(registerRequest: RegisterRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(registerRequest)
        client.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val message = responseBody.message
                        _errorMessage.value = message
                        Log.d(TAG, "onResponse: $message")
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    _errorMessage.value = message
                    Log.e(TAG, "onFailure: $message")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getAuthUser(): LiveData<String?> {
        return pref.getUser().asLiveData()
    }

    fun saveUserToken(token: String) {
        viewModelScope.launch {
            pref.saveUser(token)
        }
    }

    fun destroyUserToken() {
        viewModelScope.launch {
            pref.destroyUser()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}