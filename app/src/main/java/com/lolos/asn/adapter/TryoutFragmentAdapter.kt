package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.DataItem
import com.lolos.asn.databinding.ListTryoutBinding
import com.lolos.asn.ui.activity.ResultActivity
import com.lolos.asn.ui.activity.TryoutDetailActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

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
            val createdAt = tryout.createdAt
            val tryoutClosed = tryout.tryoutClosed

            // Separate the date and time zone offset
            val createdAtParts = createdAt?.split(" ")
            val tryoutClosedParts = tryoutClosed?.split(" ")
            val formattedCreatedAt = formatDate(createdAtParts?.get(0), createdAtParts?.get(1))
            val formattedTryoutClosed = formatDate(tryoutClosedParts?.get(0), tryoutClosedParts?.get(1))

            val dateRange = "$formattedCreatedAt - $formattedTryoutClosed"
            val isCleared = tryout.isCleared

            // Set the formatted dates to the TextView
            binding.tvTitleTryout.text = tryout.tryoutTitle
            binding.tvDateCpns.text = dateRange

            if (isCleared == "1") {
                binding.btnDetail.setOnClickListener {
                    val intent = Intent(context, ResultActivity::class.java)
                    intent.putExtra("tryout_id", tryout.tryoutId)
                    intent.putExtra("tryout_period", dateRange)
                    context.startActivity(intent)
                }
            } else {
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
            }
        }

        private fun formatDate(date: String?, time: String?): String {
            // Combine date and time parts
            val formattedDateStr = "$date $time"

            // Define the format pattern without milliseconds and time zone offset
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            try {
                // Parse the formatted date string
                val createdAtDate = inputFormat.parse(formattedDateStr)
                // Format the parsed date
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                return outputFormat.format(createdAtDate)
            } catch (e: ParseException) {
                // Handle the parsing exception
                e.printStackTrace()
            }
            return ""
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
