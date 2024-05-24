package com.lolos.asn.data.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lolos.asn.data.preference.IntroductionPreferences
import com.lolos.asn.data.viewmodel.model.IntroViewModel

class IntroViewModelFactory(private val pref: IntroductionPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IntroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IntroViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}