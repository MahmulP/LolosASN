package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.data.response.DrillingItem
import com.lolos.asn.databinding.ListDrillingBinding

class DrillingAdapter(private val context: Context) : ListAdapter<DrillingItem, DrillingAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListDrillingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)
    }

    class MyViewHolder(val binding: ListDrillingBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: DrillingItem, context: Context) {
            val time = item.waktu?.div(60)
            val thumbnail = item.latThumb

            binding.tvTitle.text = item.category?.categoryName
            binding.tvDescription.text = item.latDesc
            binding.tvTotal.text = context.getString(R.string.total_drill, item.jumlahSoal, time)

            Glide.with(binding.ivContent.context)
                .load(thumbnail)
                .error(R.drawable.no_image)
                .into(binding.ivContent)

            binding.tvDetail.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("latsol_id", item.latsolId)
                }
                val navController = (context as AppCompatActivity).findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigate(R.id.action_drilling_to_detail, bundle)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DrillingItem>() {
            override fun areItemsTheSame(oldItem: DrillingItem, newItem: DrillingItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DrillingItem, newItem: DrillingItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}