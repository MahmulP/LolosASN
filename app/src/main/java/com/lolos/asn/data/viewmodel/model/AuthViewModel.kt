package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lolos.asn.data.data.UserData
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.response.LoginRequest
import com.lolos.asn.data.response.LoginResponse
import com.lolos.asn.data.response.RegisterRequest
import com.lolos.asn.data.response.RegisterResponse
import com.lolos.asn.data.response.UpdateUserResponse
import com.lolos.asn.data.response.UserDataResponse
import com.lolos.asn.data.response.UserResponse
import com.lolos.asn.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: UserPreferences): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: LiveData<Boolean> = _isChanged

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
                        val userId = responseBody.userId
                        saveUser(token, userId)
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

    fun updateUserData(userId: String?, name: RequestBody?, email: RequestBody?, password: RequestBody?, avatar: MultipartBody.Part?) {
        val client = ApiConfig.getApiService().updateUserData(userId = userId, name = name, email = email, password = password, avatar = avatar)
        client.enqueue(object: Callback<UpdateUserResponse> {
            override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
                if (response.isSuccessful) {
                    _isChanged.value = true
                } else {
                    _isChanged.value = false
                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getAUthUserData(userId: String?) {
        val client = ApiConfig.getApiService().getAuthUserData(userId)
        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val name = responseBody.data.name
                        val role = responseBody.data.role
                        val email = responseBody.data.email
                        val avatar = responseBody.data.avatar ?: "https://yt3.googleusercontent.com/-CFTJHU7fEWb7BYEb6Jh9gm1EpetvVGQqtof0Rbh-VQRIznYYKJxCaqv_9HeBcmJmIsp2vOO9JU=s900-c-k-c0x00ffffff-no-rj"

                        saveUserData(
                            username = name,
                            role = role,
                            email = email,
                            avatar = avatar)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _errorMessage.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getAuthUser(): LiveData<UserData> {
        return pref.getAuthUser().asLiveData()
    }

    fun getUserData(): LiveData<UserDataResponse> {
        return pref.getUserData().asLiveData()
    }

    fun saveUser(token: String, userId: String) {
        viewModelScope.launch {
            pref.saveUser(token, userId)
        }
    }

    fun saveUserData(username: String, email: String, role: String, avatar: String) {
        viewModelScope.launch {
            pref.saveUserData(username, email, role, avatar)
        }
    }

    fun destroyUserToken() {
        viewModelScope.launch {
            pref.clearUser()
            pref.clearUserData()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}