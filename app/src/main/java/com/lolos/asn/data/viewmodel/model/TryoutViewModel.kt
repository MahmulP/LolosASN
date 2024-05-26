package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.Data
import com.lolos.asn.data.response.DataItem
import com.lolos.asn.data.response.TryoutResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TryoutViewModel: ViewModel() {
    private val _tryout = MutableLiveData<TryoutResponse>()
    val tryout: LiveData<TryoutResponse> = _tryout

    fun getNewestTryout() {
        val client = ApiConfig.getApiService().getNewestTryout()
        client.enqueue(object : Callback<TryoutResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<TryoutResponse>, response: Response<TryoutResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _tryout.value = responseBody
                }
            }

            override fun onFailure(call: Call<TryoutResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "TryoutViewModel"
    }
}