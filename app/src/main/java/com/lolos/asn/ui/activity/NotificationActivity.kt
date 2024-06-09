package com.lolos.asn.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.NotificationAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.NotificationResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.NotificationViewModel
import com.lolos.asn.databinding.ActivityNotificationBinding
import com.lolos.asn.utils.groupNotificationsByDate

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding

    private val notificationViewModel by viewModels<NotificationViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        authViewModel.getAuthUser().observe(this) { userData ->
            if (userData != null) {
                userId = userData.userId
                notificationViewModel.getNotification(userId)
            }
        }

        notificationViewModel.notificationData.observe(this) { notificationResponse ->
            if (notificationResponse != null) {
                setupRecycleView(notificationResponse)
            }
        }

        notificationViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        notificationViewModel.isEmpty.observe(this) { isEmpty ->
            showEmpty(isEmpty)
        }
    }

    private fun showEmpty(isEmpty: Boolean) {
        binding.emptyLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecycleView(notificationResponse: NotificationResponse) {
        binding.rvNotification.layoutManager = LinearLayoutManager(this)

        val adapter = NotificationAdapter(this, notificationViewModel)
        binding.rvNotification.adapter = adapter

        val groupedNotifications = groupNotificationsByDate(notificationResponse.data)

        adapter.setData(groupedNotifications)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}