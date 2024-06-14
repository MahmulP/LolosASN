package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.data.response.DataItem
import com.lolos.asn.databinding.ListNewestTryoutBinding
import com.lolos.asn.ui.activity.TryoutDetailActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TryoutAdapter(private val context: Context): ListAdapter<DataItem, TryoutAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListNewestTryoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tryout = getItem(position)
        holder.bind(tryout, context)
    }
    class MyViewHolder(val binding: ListNewestTryoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(tryout: DataItem, context: Context) {
            val createdAt = tryout.createdAt
            val tryoutClosed = tryout.tryoutClosed

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormatWithYear = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val outputFormatWithoutYear = SimpleDateFormat("dd MMMM", Locale("id", "ID"))

            var createdAtDate: Date? = null
            var tryoutClosedDate: Date? = null

            if (createdAt != null) {
                createdAtDate = try {
                    inputFormat.parse(createdAt)
                } catch (e: ParseException) {
                    null
                }
            }

            if (tryoutClosed != null) {
                tryoutClosedDate = try {
                    inputFormat.parse(tryoutClosed)
                } catch (e: ParseException) {
                    null
                }
            }

            val createdAtYear = createdAtDate?.let {
                SimpleDateFormat("yyyy", Locale.getDefault()).format(it)
            }
            val tryoutClosedYear = tryoutClosedDate?.let {
                SimpleDateFormat("yyyy", Locale.getDefault()).format(it)
            }

            val formattedCreatedAt = if (createdAtYear == tryoutClosedYear) {
                createdAtDate?.let { outputFormatWithoutYear.format(it) } ?: "invalid date"
            } else {
                createdAtDate?.let { outputFormatWithYear.format(it) } ?: "invalid date"
            }

            val formattedTryoutClosed = tryoutClosedDate?.let { outputFormatWithYear.format(it) } ?: "invalid date"

            val dateRange = "$formattedCreatedAt - $formattedTryoutClosed"
//            val dateRange = "$createdAt - $tryoutClosed"

            // Set the formatted dates to the TextView
            binding.tvTitleTryout.text = tryout.tryoutTitle
            binding.tvDateRange.text = dateRange

            binding.tvDetail.setOnClickListener {
                val intent = Intent(context, TryoutDetailActivity::class.java)
                intent.putExtra("tryout_id", tryout.tryoutId)
                intent.putExtra("tryout_period", dateRange)
                context.startActivity(intent)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}