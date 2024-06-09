package com.lolos.asn.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.FinishedTryout
import com.lolos.asn.databinding.ListHistoryTryoutBinding
import com.lolos.asn.ui.activity.ResultActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TryoutHistoryAdapter(private val context: Context) : ListAdapter<FinishedTryout, TryoutHistoryAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListHistoryTryoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tryout = getItem(position)
        holder.bind(tryout, context)
    }

    class MyViewHolder(val binding: ListHistoryTryoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tryout: FinishedTryout, context: Context) {
            with(binding) {
                val score = tryout.tryoutScore
                val status = if (tryout.tryoutPassed == "Passed") context.getString(R.string.passed_small) else context.getString(R.string.not_passed_small)
                tvTitle.text = tryout.tryoutTitle
                tvStatus.text = context.getString(R.string.history_status, score, status)

                tvDetail.setOnClickListener {
                    val intent = Intent(context, ResultActivity::class.java)
                    intent.putExtra("tryout_id", tryout.tryoutId)
                    context.startActivity(intent)
                }

                if (tryout.tryoutPassed != "Passed") {
                    tvStatus.backgroundTintList = ContextCompat.getColorStateList(context, R.color.red)
                }

                val patterns = listOf(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS",
                    "yyyy-MM-dd HH:mm:ss.SSSSSS",
                    "yyyy-MM-dd HH:mm:ss.SSS"
                )

                var date: Date? = null

                for (pattern in patterns) {
                    val inputFormat = SimpleDateFormat(pattern, Locale.getDefault())
                    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                    try {
                        val createdAt = tryout.createdAt.replace("Z", "+0000").let {
                            if (pattern == "yyyy-MM-dd HH:mm:ss.SSS" && it.length > 23) it.substring(0, 23) else it
                        }
                        date = inputFormat.parse(createdAt)
                        if (date != null) break // Exit loop if parsing was successful
                    } catch (e: ParseException) {
                        Log.e("NotificationViewHolder", "Date parsing failed with pattern $pattern: ${e.message}")
                    }
                }

                val formattedDate = if (date != null) {
                    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", Locale("id", "ID"))
                    outputFormat.format(date)
                } else {
                    "Invalid date"
                }

                tvDate.text = formattedDate
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FinishedTryout>() {
            override fun areItemsTheSame(oldItem: FinishedTryout, newItem: FinishedTryout): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FinishedTryout, newItem: FinishedTryout): Boolean {
                return oldItem == newItem
            }
        }
    }
}