package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.FinishedTryoutResponse
import com.lolos.asn.data.response.TryoutBundleDetailResponse
import com.lolos.asn.data.response.TryoutBundleResponse
import com.lolos.asn.data.response.TryoutResponse
import com.lolos.asn.data.response.TryoutResultResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TryoutViewModel: ViewModel() {
    private val _tryout = MutableLiveData<TryoutResponse>()
    val tryout: LiveData<TryoutResponse> = _tryout

    private val _freeTryout = MutableLiveData<TryoutResponse>()
    val freeTryout: LiveData<TryoutResponse> = _freeTryout

    private val _paidTryout = MutableLiveData<TryoutResponse?>()
    val paidTryout: LiveData<TryoutResponse?> = _paidTryout

    private val _resultTryout = MutableLiveData<TryoutResultResponse>()
    val resultTryout: LiveData<TryoutResultResponse> = _resultTryout

    private val _finishedTryout = MutableLiveData<FinishedTryoutResponse>()
    val finishedTryout: LiveData<FinishedTryoutResponse> = _finishedTryout

    private val _bundleTryout = MutableLiveData<TryoutBundleResponse>()
    val bundleTryout: LiveData<TryoutBundleResponse> = _bundleTryout

    private val _bundleTryoutDetail = MutableLiveData<TryoutBundleDetailResponse>()
    val bundleTryoutDetail: LiveData<TryoutBundleDetailResponse> = _bundleTryoutDetail

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

    fun getPaidTryout(userId: String?) {
        val client = ApiConfig.getApiService().getPaidTryout(userId)
        client.enqueue(object : Callback<TryoutResponse> {
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

    fun getFreeTryout(userId: String?) {
        val client = ApiConfig.getApiService().getFreeTryout(userId)
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

    fun getResultTryout(tryoutId: String?, userId: String?) {
        val client = ApiConfig.getApiService().getTryoutResult(tryoutId = tryoutId, userId = userId)
        client.enqueue(object : Callback<TryoutResultResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<TryoutResultResponse>, response: Response<TryoutResultResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _resultTryout.value = responseBody
                }
            }

            override fun onFailure(call: Call<TryoutResultResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFinishedTryout(userId: String?) {
        val client = ApiConfig.getApiService().getFinishedTryout(userId = userId)
        client.enqueue(object : Callback<FinishedTryoutResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<FinishedTryoutResponse>, response: Response<FinishedTryoutResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _finishedTryout.value = responseBody
                }
            }

            override fun onFailure(call: Call<FinishedTryoutResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getBundleTryout(userId: String?) {
        val client = ApiConfig.getApiService().getBundle(userId = userId)
        client.enqueue(object : Callback<TryoutBundleResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<TryoutBundleResponse>, response: Response<TryoutBundleResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _bundleTryout.value = responseBody
                }
            }

            override fun onFailure(call: Call<TryoutBundleResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getBundleTryoutDetail(userId: String?, bundleId: String?) {
        val client = ApiConfig.getApiService().getDetailBundle(userId = userId, bundleId = bundleId)
        client.enqueue(object : Callback<TryoutBundleDetailResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<TryoutBundleDetailResponse>, response: Response<TryoutBundleDetailResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _bundleTryoutDetail.value = responseBody
                }
            }

            override fun onFailure(call: Call<TryoutBundleDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "TryoutViewModel"
    }
}