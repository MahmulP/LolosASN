package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.TransactionItem
import com.lolos.asn.databinding.ListHistoryTransactionBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionHistoryAdapter(private val context: Context) :
    ListAdapter<TransactionItem, TransactionHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListHistoryTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val leaderboard = getItem(position)
        holder.bind(leaderboard, position)
    }

    class MyViewHolder(val binding: ListHistoryTransactionBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: TransactionItem, position: Int) {
            val createdAt = item.createdAt
            val price = item.transactionPrice
            val indonesianLocale = Locale("in", "ID")
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", indonesianLocale)
            val createdAtDate = inputFormat.parse(createdAt.toString())
            val formattedCreatedAt = createdAtDate?.let { outputFormat.format(it) }

            val formattedPrice = NumberFormat.getNumberInstance(indonesianLocale).format(price)

            binding.tvTitle.text = item.transactionTitle
            binding.tvDate.text = formattedCreatedAt
            binding.tvPrice.text = context.getString(R.string.price, formattedPrice)
            binding.tvStatus.text = item.transactionStatus
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionItem>() {
            override fun areItemsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
                return oldItem.transactionRecordId == newItem.transactionRecordId
            }

            override fun areContentsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
