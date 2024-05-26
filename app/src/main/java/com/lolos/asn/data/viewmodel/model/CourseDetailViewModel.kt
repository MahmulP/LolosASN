package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolos.asn.data.response.CourseDetailResponse
import com.lolos.asn.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseDetailViewModel: ViewModel() {
    private val _courseDetail = MutableLiveData<CourseDetailResponse?>()
    val courseDetail: LiveData<CourseDetailResponse?> = _courseDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getDetailCourse(courseId: String?, userId: String?) {
        val client = ApiConfig.getApiService().getDetailCourse(courseId, userId)
        _isLoading.value = true
        client.enqueue(object : Callback<CourseDetailResponse> {
            override fun onResponse(
                call: Call<CourseDetailResponse>,
                response: Response<CourseDetailResponse>,
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    _courseDetail.value = responseBody
                }
            }

            override fun onFailure(call: Call<CourseDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "CourseDetailViewModel"
    }
}