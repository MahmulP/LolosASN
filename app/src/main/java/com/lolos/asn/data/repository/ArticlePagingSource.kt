package com.lolos.asn.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lolos.asn.data.response.ArticleResponseItem
import com.lolos.asn.data.retrofit.ApiService
import retrofit2.await

class ArticlePagingSource(private val apiService: ApiService) : PagingSource<Int, ArticleResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleResponseItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            Log.d("ArticlePagingSource", "Loading page $position")
            val response = apiService.getNewestArticles(position, params.loadSize).await()
            Log.d("ArticlePagingSource", "Received response: $response")
            val responseData = response.items?.filterNotNull() ?: emptyList()
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            Log.e("ArticlePagingSource", "Error loading data", exception)
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
