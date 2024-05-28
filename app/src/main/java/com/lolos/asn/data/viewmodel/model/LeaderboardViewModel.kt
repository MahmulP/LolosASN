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
    
    fun getLeaderboardData(tryoutId: String?) {
        val client = ApiConfig.getApiService().getTryoutLeaderboard(tryoutId)
        client.enqueue(object : Callback<LeaderboardResponse> {
            override fun onResponse(
                call: Call<LeaderboardResponse>,
                response: Response<LeaderboardResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _leaderboardData.value = responseBody
                }
            }

            override fun onFailure(call: Call<LeaderboardResponse>, t: Throwable) {
                Log.e("LeaderboardViewModel", "onFailure: ${t.message}")
            }
        })
    }
}