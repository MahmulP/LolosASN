package com.lolos.asn.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.databinding.ListChoiceDiscussionBinding

class DiscussionAnswerAdapter : ListAdapter<String, DiscussionAnswerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var options: List<String?> = emptyList()
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListChoiceDiscussionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(options[position], position, selectedPosition)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    fun setOptions(newOptions: List<String?>, selectedAnswer: Int?) {
        options = newOptions
        selectedPosition = selectedAnswer ?: -1
    }

    inner class MyViewHolder(val binding: ListChoiceDiscussionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: String?, position: Int, rightAnswer: Int) {
            binding.tvChoiceText.text = option
            binding.tvChoice.text = getLetterForPosition(position)

            if (position == rightAnswer) {
                val primaryColor = ContextCompat.getColor(binding.root.context, R.color.primaryColor)
                val white = ContextCompat.getColor(binding.root.context, R.color.white)
                binding.cvOption.backgroundTintList = ColorStateList.valueOf(primaryColor)
                binding.tvChoice.backgroundTintList = ColorStateList.valueOf(white)
                binding.tvChoiceText.setTextColor(white)
            }
        }

        private fun getLetterForPosition(position: Int): String {
            return when (position) {
                0 -> "A"
                1 -> "B"
                2 -> "C"
                3 -> "D"
                else -> "E"
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}