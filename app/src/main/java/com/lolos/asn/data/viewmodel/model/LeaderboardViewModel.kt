package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.LeaderboardResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardViewModel: ViewModel() {
    private val _leaderboardData = MutableLiveData<LeaderboardResponse?>()
    val leaderboardData: LiveData<LeaderboardResponse?> = _leaderboardData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty
    
    fun getLeaderboardData(tryoutId: String?, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getTryoutLeaderboard(tryoutId, token)
        client.enqueue(object : Callback<LeaderboardResponse> {
            override fun onResponse(
                call: Call<LeaderboardResponse>,
                response: Response<LeaderboardResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _leaderboardData.value = responseBody
                    } else {
                        _isEmpty.value = true
                    }
                }
            }

            override fun onFailure(call: Call<LeaderboardResponse>, t: Throwable) {
                Log.e("LeaderboardViewModel", "onFailure: ${t.message}")
                _isLoading.value = false
                _isEmpty.value = true
            }
        })
    }
}