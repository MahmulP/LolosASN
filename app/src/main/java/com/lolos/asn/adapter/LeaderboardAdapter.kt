package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.data.response.DataItem
import com.lolos.asn.data.response.LeaderboardItem
import com.lolos.asn.databinding.LeaderboardItemBinding

class LeaderboardAdapter(private val context: Context) :
    ListAdapter<LeaderboardItem, LeaderboardAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = LeaderboardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val leaderboard = getItem(position)
        holder.bind(leaderboard, position)
    }

    class MyViewHolder(val binding: LeaderboardItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: LeaderboardItem, position: Int) {
            binding.userName.text = item.accountId
            binding.tvRank.text = "${position + 1}"
            binding.tvScore.text = item.tryoutScore.toString()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LeaderboardItem>() {
            override fun areItemsTheSame(oldItem: LeaderboardItem, newItem: LeaderboardItem): Boolean {
                return oldItem.tryoutScoreId == newItem.tryoutScoreId
            }

            override fun areContentsTheSame(oldItem: LeaderboardItem, newItem: LeaderboardItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
