package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.TryoutDetailResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TryoutDetailViewModel: ViewModel() {
    private val _tryout = MutableLiveData<TryoutDetailResponse>()
    val tryout: LiveData<TryoutDetailResponse> = _tryout

    fun getTryoutDetail(tryoutId: String?, userId: String?, token: String) {
        val client = ApiConfig.getApiService().getDetailTryout(tryoutId, userId, token)
        client.enqueue(object : Callback<TryoutDetailResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<TryoutDetailResponse>, response: Response<TryoutDetailResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _tryout.value = responseBody
                }
            }

            override fun onFailure(call: Call<TryoutDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "TryoutViewModel"
    }
}