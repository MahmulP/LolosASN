package com.lolos.asn.data.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lolos.asn.data.preference.IntroductionPreferences
import kotlinx.coroutines.launch

class IntroViewModel(private val pref: IntroductionPreferences): ViewModel() {
    fun getIntroStatus(): LiveData<Boolean?> {
        return pref.getIntroStatus().asLiveData()
    }

    fun changeStatus(status: Boolean) {
        viewModelScope.launch {
            pref.saveStatus(status)
        }
    }
}