package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.CourseResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class CourseViewModel: ViewModel() {
    private val _courses = MutableLiveData<CourseResponse>()
    val courses : LiveData<CourseResponse> = _courses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun getCourses(userId: String, categoryId: String, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getCourses(userId, categoryId, token)
        client.enqueue(object : retrofit2.Callback<CourseResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<CourseResponse>, response: Response<CourseResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    _courses.value = responseBody
                }
            }

            override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                _isLoading.value = false
                _isEmpty.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "CourseViewModel"
    }
}