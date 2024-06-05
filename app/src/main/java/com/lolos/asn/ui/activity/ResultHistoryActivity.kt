package com.lolos.asn.ui.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.NotificationViewModel
import com.lolos.asn.databinding.ActivityResultHistoryBinding

class ResultHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultHistoryBinding
    private val notificationViewModel by viewModels<NotificationViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.getAuthUser().observe(this) { userData ->
            if (userData.userId != null) {
                notificationViewModel.getNotification(userData.userId)
                userId = userData.userId
            }
        }

        notificationViewModel.notificationItem.observe(this) { item ->
            if (item != null) {
                if (item.accountId == userId) {
                    val message = item.notifikasiMsg
                    Log.d("Notification", "Received Notification: $item")
                    sendNotification(message)
                } else {
                    Log.e("Notification", "onCreate: NOT FOR U")
                }
            }
        }
    }

    private fun sendNotification(message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val title = getString(R.string.app_name)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "dicoding channel"
    }
}