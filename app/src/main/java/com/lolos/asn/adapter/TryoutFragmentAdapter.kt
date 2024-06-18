package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.DataItem
import com.lolos.asn.databinding.ListTryoutBinding
import com.lolos.asn.ui.activity.PurchaseActivity
import com.lolos.asn.ui.activity.ResultActivity
import com.lolos.asn.ui.activity.TryoutDetailActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TryoutFragmentAdapter(private val context: Context) : ListAdapter<DataItem, TryoutFragmentAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListTryoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tryout = getItem(position)
        holder.bind(tryout, context)
    }

    class MyViewHolder(val binding: ListTryoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(tryout: DataItem, context: Context) {
            val createdAtFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
            val tryoutClosedFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val createdAtOutputFormat = SimpleDateFormat("dd MMMM", Locale("id", "ID"))
            val tryoutClosedOutputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

            createdAtOutputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
            tryoutClosedOutputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

            val createdAt = tryout.createdAt
            val tryoutClosed = tryout.tryoutClosed

            val createdAtDate = createdAt?.let { createdAtFormat.parse(it) }
            val tryoutClosedDate = tryoutClosed?.let { tryoutClosedFormat.parse(it) }

            val formattedCreatedAt = createdAtDate?.let { createdAtOutputFormat.format(it) } ?: "invalid date"
            val formattedTryoutClosed = tryoutClosedDate?.let { tryoutClosedOutputFormat.format(it) } ?: "invalid date"

            val dateRange = "$formattedCreatedAt - $formattedTryoutClosed"

            val isCleared = tryout.isCleared
            val isAccessed = tryout.accessed

            // Set the formatted dates to the TextView
            binding.tvTitleTryout.text = tryout.tryoutTitle
            binding.tvDateCpns.text = dateRange

            if (isAccessed != null && isCleared != null) {
                if (isCleared >= "1" && isAccessed >= "1") {
                    binding.btnDetail.setOnClickListener {
                        val intent = Intent(context, ResultActivity::class.java)
                        intent.putExtra("tryout_id", tryout.tryoutId)
                        intent.putExtra("tryout_period", dateRange)
                        context.startActivity(intent)
                    }
                } else if (isAccessed >= "1") {
                    binding.btnDetail.text = "Mulai"
                    binding.btnDetail.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.btnDetail.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primaryColor
                        )
                    )
                    binding.btnDetail.setOnClickListener {
                        val intent = Intent(context, TryoutDetailActivity::class.java)
                        intent.putExtra("tryout_id", tryout.tryoutId)
                        intent.putExtra("tryout_period", dateRange)
                        context.startActivity(intent)
                    }
                } else if (tryout.tryoutType == "FREE" && isAccessed == "0") {
                    binding.btnDetail.text = context.getString(R.string.request_now)
                    binding.btnDetail.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.btnDetail.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primaryColor
                        )
                    )
                    binding.btnDetail.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://pasti.lolosasn.id"))
                        context.startActivity(intent)
                    }
                } else {
                    binding.btnDetail.text = context.getString(R.string.buy_now)
                    binding.btnDetail.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.btnDetail.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primaryColor
                        )
                    )
                    binding.btnDetail.setOnClickListener {
                        val intent = Intent(context, PurchaseActivity::class.java)
                        context.startActivity(intent)
                    }
                }
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
