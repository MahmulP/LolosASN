package com.lolos.asn.data.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.viewmodel.model.AuthViewModel

class AuthViewModelFactory(private val pref: UserPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
