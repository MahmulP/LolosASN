package com.lolos.asn.data.viewmodel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.lolos.asn.data.response.NotificationItem
import com.lolos.asn.data.response.NotificationResponse
import com.lolos.asn.data.retrofit.ApiConfig
import com.lolos.asn.utils.WebSocketClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel: ViewModel() {
    private val _notificationData = MutableLiveData<NotificationResponse?>()
    val notificationData: LiveData<NotificationResponse?> = _notificationData

    private val _notificationItem = MutableLiveData<NotificationItem?>()
    val notificationItem: LiveData<NotificationItem?> = _notificationItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val webSocketClient: WebSocketClient

    init {
        webSocketClient = WebSocketClient { message ->
            handleWebSocketMessage(message)
        }
        webSocketClient.start()
    }

    private fun handleWebSocketMessage(message: String) {
        try {
            val notificationResponse = parseNotificationResponse(message)
            _notificationItem.postValue(notificationResponse)
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "Error parsing WebSocket message: $message")
        }
    }

    private fun parseNotificationResponse(json: String): NotificationItem? {
        return try {
            Gson().fromJson(json, NotificationItem::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
    
    fun getNotification(userId: String?, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNotification(userId, token)
        client.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody?.data != null && responseBody.data.isNotEmpty()) {
                        _notificationData.value = responseBody
                    } else {
                        _isEmpty.value = true
                    }
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
                _isEmpty.value = true
            }
        })
    }

    fun updateNotification(userId: String?, notificationId: String?, token: String) {
        val client = ApiConfig.getApiService().updateNotification(userId = userId, notificationId = notificationId, token = token)
        client.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        private const val TAG = "NotificationViewModel"
    }
}