package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.DrillingDetailResponse
import com.lolos.asn.data.response.DrillingHistoryResponse
import com.lolos.asn.data.response.DrillingResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DrillingViewModel: ViewModel() {
    private val _drilling = MutableLiveData<DrillingResponse?>()
    val drilling: LiveData<DrillingResponse?> = _drilling

    private val _drillingDetail = MutableLiveData<DrillingDetailResponse?>()
    val drillingDetail: LiveData<DrillingDetailResponse?> = _drillingDetail

    private val _drillingHistory = MutableLiveData<DrillingHistoryResponse?>()
    val drillingHistory: LiveData<DrillingHistoryResponse?> = _drillingHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun getAllDrilling(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllDrilling(token)
        client.enqueue(object : Callback<DrillingResponse> {
            override fun onResponse(
                call: Call<DrillingResponse>,
                response: Response<DrillingResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody?.data != null && responseBody.data.isNotEmpty()) {
                        _drilling.value = responseBody
                    } else {
                        _isEmpty.value = true
                    }
                }
            }

            override fun onFailure(call: Call<DrillingResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
                _isEmpty.value = true
            }
        })
    }

    fun getDrillingDetail(latsolId: String?, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailDrilling(latsolId = latsolId, token = token)
        client.enqueue(object : Callback<DrillingDetailResponse> {
            override fun onResponse(
                call: Call<DrillingDetailResponse>,
                response: Response<DrillingDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody?.data != null) {
                        _drillingDetail.value = responseBody
                    } else {
                        _isEmpty.value = true
                    }
                }
            }

            override fun onFailure(call: Call<DrillingDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
                _isEmpty.value = true
            }
        })
    }

    fun getHistoryDrilling(latsolId: String?, userId: String?, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getHistoryDrilling(latsolId = latsolId, userId = userId, token = token)
        client.enqueue(object : Callback<DrillingHistoryResponse> {
            override fun onResponse(
                call: Call<DrillingHistoryResponse>,
                response: Response<DrillingHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody?.data?.isEmpty() == false) {
                        _drillingHistory.value = responseBody
                    } else {
                        _isEmpty.value = true
                    }
                }
            }

            override fun onFailure(call: Call<DrillingHistoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
                _isEmpty.value = true
            }
        })
    }

    companion object {
        private const val TAG = "DrillingViewModel"
    }
}