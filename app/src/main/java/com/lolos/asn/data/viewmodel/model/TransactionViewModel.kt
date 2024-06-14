package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.TransactionHistoryResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionViewModel: ViewModel() {
    private val _transactionHistory = MutableLiveData<TransactionHistoryResponse?>()
    val transactionHistory: LiveData<TransactionHistoryResponse?> = _transactionHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun getTransactionHistory(userId: String?, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getTransactionHistory(userId = userId, token = token)
        client.enqueue(object : Callback<TransactionHistoryResponse> {
            override fun onResponse(
                call: Call<TransactionHistoryResponse>,
                response: Response<TransactionHistoryResponse>,
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody?.data?.isEmpty() == true) {
                        _isEmpty.value = true
                    } else {
                        _transactionHistory.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<TransactionHistoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
                _isEmpty.value = true
            }

        })
    }

    companion object {
        private const val TAG = "TransactionViewModel"
    }
}