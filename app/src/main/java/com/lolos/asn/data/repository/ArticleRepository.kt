package com.lolos.asn.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.lolos.asn.data.response.ArticleResponseItem
import com.lolos.asn.data.retrofit.ApiService

class ArticleRepository(private val apiService: ApiService) {
    fun getArticle(): LiveData<PagingData<ArticleResponseItem>> {
        Log.d("ArticleRepository", "Fetching articles...")
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ArticlePagingSource(apiService).also { Log.d("ArticleRepository", "Created PagingSource") }
            }
        ).liveData
    }
}


