package com.lolos.asn.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.NotificationDisplayItem
import com.lolos.asn.data.response.NotificationItem
import com.lolos.asn.databinding.ListHeaderBinding
import com.lolos.asn.databinding.ListNotificationBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationAdapter(private val context: Context) : ListAdapter<NotificationDisplayItem, RecyclerView.ViewHolder>(
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
            is NotificationDisplayItem.DisplayItem -> (holder as NotificationViewHolder).bind(item.notification, context)
        }
    }

    class HeaderViewHolder(val binding: ListHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.tvHeaderTitle.text = title
        }
    }

    class NotificationViewHolder(val binding: ListNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationItem, context: Context) {
            with(binding) {
                tvTitle.text = context.getString(R.string.app_name)
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
            }
        }
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
