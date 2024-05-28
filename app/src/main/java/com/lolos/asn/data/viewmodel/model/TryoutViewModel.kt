package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.Data
import com.lolos.asn.data.response.DataItem
import com.lolos.asn.data.response.ExaminationResponse
import com.lolos.asn.data.response.TryoutResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TryoutViewModel: ViewModel() {
    private val _tryout = MutableLiveData<TryoutResponse>()
    val tryout: LiveData<TryoutResponse> = _tryout

    private val _freeTryout = MutableLiveData<TryoutResponse>()
    val freeTryout: LiveData<TryoutResponse> = _freeTryout

    private val _paidTryout = MutableLiveData<TryoutResponse>()
    val paidTryout: LiveData<TryoutResponse> = _paidTryout

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

    fun getPaidTryout() {
        val client = ApiConfig.getApiService().getPaidTryout()
        client.enqueue(object : Callback<TryoutResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<TryoutResponse>, response: Response<TryoutResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _paidTryout.value = responseBody
                }
            }

            override fun onFailure(call: Call<TryoutResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFreeTryout() {
        val client = ApiConfig.getApiService().getFreeTryout()
        client.enqueue(object : Callback<TryoutResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<TryoutResponse>, response: Response<TryoutResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _freeTryout.value = responseBody
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