package com.lolos.asn.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.NotificationDisplayItem
import com.lolos.asn.data.response.NotificationItem
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.NotificationViewModel
import com.lolos.asn.databinding.ListHeaderBinding
import com.lolos.asn.databinding.ListNotificationBinding
import com.lolos.asn.ui.activity.HistoryActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationAdapter(private val context: Context, private val notificationViewModel: NotificationViewModel, private val authViewModel: AuthViewModel, private val lifecycleOwner: LifecycleOwner,) : ListAdapter<NotificationDisplayItem, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NotificationDisplayItem.Header -> VIEW_TYPE_HEADER
            is NotificationDisplayItem.DisplayItem -> VIEW_TYPE_NOTIFICATION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_NOTIFICATION -> {
                val binding = ListNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NotificationViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is NotificationDisplayItem.Header -> (holder as HeaderViewHolder).bind(item.title)
            is NotificationDisplayItem.DisplayItem -> (holder as NotificationViewHolder).bind(item.notification, context, notificationViewModel, authViewModel, lifecycleOwner)
        }
    }

    class HeaderViewHolder(val binding: ListHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.tvHeaderTitle.text = title
        }
    }

    class NotificationViewHolder(val binding: ListNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationItem, context: Context, notificationViewModel: NotificationViewModel, authViewModel: AuthViewModel, lifecycleOwner: LifecycleOwner) {
            with(binding) {
                tvTitle.text = notification.notificationTitle
                tvContent.text = notification.notifikasiMsg

                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                var date: Date? = null
                try {
                    date = inputFormat.parse(notification.createdAt)
                } catch (e: ParseException) {
                    Log.e("NotificationViewHolder", "Date parsing failed: ${e.message}")
                }

                val formattedDate = if (date != null) {
                    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
                    outputFormat.format(date)
                } else {
                    "Invalid date"
                }

                tvDate.text = formattedDate

                if (notification.isClicked == false) {
                    val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)
                    val secondPrimary = ContextCompat.getColor(context, R.color.secondPrimary)
                    val white = ContextCompat.getColor(context, R.color.white)

                    cvNotification.setBackgroundColor(secondPrimary)
                    icNotification.imageTintList = ColorStateList.valueOf(primaryColor)
                    icNotification.backgroundTintList = ColorStateList.valueOf(white)
                    divider.visibility = View.INVISIBLE

                    cvNotification.setOnClickListener {
                        authViewModel.getAuthUser().observe(lifecycleOwner) {
                            if (it.token != null) {
                                val token = it.token
                                notificationViewModel.updateNotification(userId = notification.accountId, notificationId = notification.notifikasiId, token = token)
                                notificationViewModel.getNotification(userId = notification.accountId, token = token)
                            }
                        }

                        if (notification.notificationTitle?.contains("Transaksi") == true) {
                            context.startActivity(Intent(context, HistoryActivity::class.java))
                        }
                    }
                } else {
                    cvNotification.setOnClickListener {
                        if (notification.notificationTitle?.contains("Transaksi") == true) {
                            val intent = Intent(context, HistoryActivity::class.java)
                            intent.putExtra("intentFrom", "notification")
                            context.startActivity(intent)
                        }
                    }
                }

            }
        }
    }

    fun setData(items: List<NotificationDisplayItem>) {
        val headers = items.filterIsInstance<NotificationDisplayItem.Header>()
        val displayItems = items.filterIsInstance<NotificationDisplayItem.DisplayItem>()

        val sortedDisplayItems = displayItems.sortedWith(compareByDescending {
            it.notification.createdAt.let { createdAt ->
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(createdAt)
            } ?: Date(0)
        })

        val mergedList = mutableListOf<NotificationDisplayItem>()
        var displayItemIndex = 0

        items.forEach { item ->
            when (item) {
                is NotificationDisplayItem.Header -> mergedList.add(item)
                is NotificationDisplayItem.DisplayItem -> mergedList.add(sortedDisplayItems[displayItemIndex++])
            }
        }

        submitList(mergedList)
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_NOTIFICATION = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NotificationDisplayItem>() {
            override fun areItemsTheSame(oldItem: NotificationDisplayItem, newItem: NotificationDisplayItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NotificationDisplayItem, newItem: NotificationDisplayItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
