package com.lolos.asn.data.injection

import android.content.Context
import com.lolos.asn.data.repository.ArticleRepository
import com.lolos.asn.data.retrofit.AnalysisApiConfig

object Injection {
    fun provideRepository(context: Context): ArticleRepository {
        val apiService = AnalysisApiConfig.getApiService()
        return ArticleRepository(apiService)
    }
}
