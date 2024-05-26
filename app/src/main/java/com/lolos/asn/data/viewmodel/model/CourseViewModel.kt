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

    fun getCourses(userId: String, categoryId: String) {
        val client = ApiConfig.getApiService().getCourses(userId, categoryId)
        client.enqueue(object : retrofit2.Callback<CourseResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(call: Call<CourseResponse>, response: Response<CourseResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _courses.value = responseBody
                }
            }

            override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "CourseViewModel"
    }
}