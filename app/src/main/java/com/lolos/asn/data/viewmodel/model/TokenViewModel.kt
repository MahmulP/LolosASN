package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.TokenRequest
import com.lolos.asn.data.response.TokenResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenViewModel: ViewModel() {
    private val _tokenMessage = MutableLiveData<TokenResponse?>()
    val tokenMessage: LiveData<TokenResponse?> = _tokenMessage

    private val _isRedeem = MutableLiveData<Boolean>()
    val isRedeem: LiveData<Boolean> = _isRedeem

    private val _isFailed = MutableLiveData<Boolean>()
    val isFailed: LiveData<Boolean> = _isFailed

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isRedeem

    fun redeemToken(userId: String?, tokenRequest: TokenRequest, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().redeemToken(userId = userId, request = tokenRequest, token = token)
        client.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _isRedeem.value = true
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _tokenMessage.value = responseBody
                        _isLoading.value = false
                    }
                } else {
                    _isFailed.value = true
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private const val TAG = "TokenViewModel"
    }
}