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
import java.text.SimpleDateFormat
import java.util.Locale

class TryoutFragmentAdapter(private val context: Context): ListAdapter<DataItem, TryoutFragmentAdapter.MyViewHolder>(
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

            // Input and output date formats
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormatWithYear = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val outputFormatWithoutYear = SimpleDateFormat("dd MMMM", Locale("id", "ID"))

            // Parse the date strings
            val createdAtDate = inputFormat.parse(createdAt)
            val tryoutClosedDate = inputFormat.parse(tryoutClosed)

            // Format the dates
            val createdAtYear = createdAtDate?.let {
                SimpleDateFormat("yyyy", Locale.getDefault()).format(
                    it
                )
            }
            val tryoutClosedYear = tryoutClosedDate?.let {
                SimpleDateFormat("yyyy", Locale.getDefault()).format(
                    it
                )
            }

            val formattedCreatedAt = if (createdAtYear == tryoutClosedYear) {
                createdAtDate?.let { outputFormatWithoutYear.format(it) }
            } else {
                createdAtDate?.let { outputFormatWithYear.format(it) }
            }

            val formattedTryoutClosed = tryoutClosedDate?.let { outputFormatWithYear.format(it) }

            val dateRange = "$formattedCreatedAt - $formattedTryoutClosed"
            val isCleared = tryout.isCleared

            // Set the formatted dates to the TextView
            binding.tvTitleTryout.text = tryout.tryoutTitle
            binding.tvDateCpns.text = dateRange

            if (isCleared == "0") {
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