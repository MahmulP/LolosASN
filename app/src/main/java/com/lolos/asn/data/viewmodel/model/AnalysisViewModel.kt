package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.AnalysisResponse
import com.lolos.asn.data.retrofit.AnalysisApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalysisViewModel: ViewModel() {
    private val _analysisAi = MutableLiveData<AnalysisResponse>()
    val analysisAi: LiveData<AnalysisResponse> = _analysisAi
    
    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAnalysis(tryoutId: String?, userId: String?, token: String) {
        val client = AnalysisApiConfig.getApiService().getAnalysis(tryoutId = tryoutId, userId = userId, token = token)
        client.enqueue(object : Callback<AnalysisResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<AnalysisResponse>, response: Response<AnalysisResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _analysisAi.value = responseBody
                }
            }

            override fun onFailure(call: Call<AnalysisResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "AnalysisViewModel"
    }
}