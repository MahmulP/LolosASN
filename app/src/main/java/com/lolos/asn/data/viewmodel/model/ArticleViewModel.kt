package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.PopularArticleResponse
import com.lolos.asn.data.retrofit.AnalysisApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel: ViewModel() {
    private val _popularArticle = MutableLiveData<List<PopularArticleResponse>?>()
    val popularArticle: LiveData<List<PopularArticleResponse>?> = _popularArticle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun getPopularArticle(token: String) {
        _isLoading.value = true
        val client = AnalysisApiConfig.getApiService().getPopularArticles(token)
        client.enqueue(object : Callback<List<PopularArticleResponse>> {
            override fun onResponse(
                call: Call<List<PopularArticleResponse>>,
                response: Response<List<PopularArticleResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _popularArticle.value = responseBody
                    } else {
                        _isEmpty.value = true
                    }
                } else {
                    Log.e(TAG, "onResponse: ${response.errorBody()?.string()}")
                    _isLoading.value = false
                    _isEmpty.value = true
                }
            }

            override fun onFailure(call: Call<List<PopularArticleResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "ArticleViewModel"
    }
}