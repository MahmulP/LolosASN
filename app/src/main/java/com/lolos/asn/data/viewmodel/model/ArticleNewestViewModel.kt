package com.lolos.asn.data.viewmodel.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lolos.asn.data.injection.Injection
import com.lolos.asn.data.repository.ArticleRepository
import com.lolos.asn.data.response.ArticleResponseItem

class ArticleNewestViewModel(articleRepository: ArticleRepository) : ViewModel() {
    val article: LiveData<PagingData<ArticleResponseItem>> =
        articleRepository.getArticle().cachedIn(viewModelScope).also {
            Log.d("ArticleNewestViewModel", "LiveData initialized")
        }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleNewestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleNewestViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
