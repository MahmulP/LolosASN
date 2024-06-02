package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lolos.asn.R
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
            val accountAvatar = item.account?.avatar
            val fullName = item.account?.name ?: ""
            val nameParts = fullName.split(" ")
            val displayName = if (nameParts.size >= 2) {
                "${nameParts[0]} ${nameParts[1]}"
            } else {
                fullName
            }

            binding.userName.text = displayName
            binding.tvRank.text = "${position + 1}"
            binding.tvScore.text = item.tryoutScore.toString()
            Glide.with(binding.ivUser.context)
                .load(accountAvatar)
                .error(R.drawable.avatar)
                .into(binding.ivUser)

            if (item.tryoutPassed != "Passed") {
                val colorRed100 = ContextCompat.getColor(context, R.color.red_100)
                val drawable = ContextCompat.getDrawable(context, R.drawable.person_cry)

                binding.cvPoint.backgroundTintList = ColorStateList.valueOf(colorRed100)
                binding.tvScore.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)))
                binding.ivTrophy.setImageDrawable(drawable)
            }

            if (position in 0..2) {
                val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primaryColor))
                binding.tvRank.setTextColor(colorStateList)
                val robotoBold = ResourcesCompat.getFont(context, R.font.roboto_bold)
                binding.tvRank.typeface = robotoBold
                binding.tvRank.textSize = 25f
            }

            when (position) {
                0 -> binding.tvRank.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                1 -> binding.tvRank.setTextColor(ContextCompat.getColor(context, R.color.secondPrimary))
                2 -> binding.tvRank.setTextColor(ContextCompat.getColor(context, R.color.purple))
            }
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
