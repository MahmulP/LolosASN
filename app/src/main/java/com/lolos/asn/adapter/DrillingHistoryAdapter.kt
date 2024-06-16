package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.data.response.HistoryItem
import com.lolos.asn.databinding.ListHistoryDrillingBinding
import com.lolos.asn.ui.activity.DrillingResultActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DrillingHistoryAdapter(private val context: Context) : ListAdapter<HistoryItem, DrillingHistoryAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListHistoryDrillingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DrillingResultActivity::class.java)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(val binding: ListHistoryDrillingBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: HistoryItem, context: Context) {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("UTC")

            val targetFormat = SimpleDateFormat("HH.mm 'WIB' | dd MMMM yyyy", Locale("id", "ID"))
            targetFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

            val date = item.createdAt?.let { originalFormat.parse(it) }
            val formattedDate = date?.let { targetFormat.format(it) }

            binding.tvDate.text = formattedDate
            binding.tvScore.text = item.totalScore.toString()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}