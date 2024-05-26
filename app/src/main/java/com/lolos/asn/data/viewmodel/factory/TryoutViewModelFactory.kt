package com.lolos.asn.data.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lolos.asn.data.viewmodel.model.TryoutViewModel

class TryoutViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TryoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TryoutViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}