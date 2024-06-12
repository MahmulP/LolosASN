package com.lolos.asn.service.foreground

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.NotificationViewModel
import com.lolos.asn.ui.activity.HistoryActivity
import com.lolos.asn.ui.activity.MainActivity
import kotlinx.coroutines.Job

class WebSocketService : LifecycleService() {

    private val serviceJob = Job()

    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var authViewModel: AuthViewModel
    private var userId: String? = null

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        notificationViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(NotificationViewModel::class.java)

        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        authViewModel = ViewModelProvider(ViewModelStore(), AuthViewModelFactory(pref))[AuthViewModel::class.java]

        // Observe LiveData using a custom lifecycle observer
        authViewModel.getAuthUser().observe(this) { authUser ->
            userId = authUser?.userId
        }

        notificationViewModel.notificationItem.observe(this) { item ->
            if (item?.accountId == userId) {
                val message = item?.notifikasiMsg
                val title = item?.notificationTitle
                if (message != null && message != "Unknown") {
                    sendNotification(title, message)
                    NOTIFICATION_ID += 1
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Log.d(TAG, "onDestroy: Service dihentikan")
    }

    private fun sendNotification(title: String?, message: String?) {
        var intent: Intent? = null

        if (title?.contains("Transaksi") == true) {
            intent = Intent(this, HistoryActivity::class.java)
        } else {
            intent = Intent(this, MainActivity::class.java)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.app_logo)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

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
        internal val TAG = WebSocketService::class.java.simpleName
        private var NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "LolosASN Channel"
    }
}
