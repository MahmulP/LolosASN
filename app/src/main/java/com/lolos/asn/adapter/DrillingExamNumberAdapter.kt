package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.LatContentItemItem
import com.lolos.asn.data.viewmodel.model.DrillingStartViewModel
import com.lolos.asn.databinding.ListTopNumberBinding
import com.lolos.asn.ui.activity.DrillingExamActivity

class DrillingExamNumberAdapter(
    private val parentActivity: DrillingExamActivity,
    private val viewModel: DrillingStartViewModel
) : ListAdapter<LatContentItemItem, DrillingExamNumberAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListTopNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, viewModel, parentActivity)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val examination = getItem(position)
        holder.bind(examination, position)
    }

    class MyViewHolder(
        val binding: ListTopNumberBinding,
        private val viewModel: DrillingStartViewModel,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            viewModel.selectedAnswers.observe(lifecycleOwner) { selectedAnswers ->
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val isFilled = selectedAnswers?.containsKey(position) == true
                    updateBackgroundTint(isFilled)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(examination: LatContentItemItem, position: Int) {
            binding.tvNumber.text = (position + 1).toString()

            val isFilled = viewModel.isAnswerFilled(position)
            updateBackgroundTint(isFilled)
        }

        private fun updateBackgroundTint(isFilled: Boolean) {
            val context = binding.root.context
            val color = if (isFilled) {
                ContextCompat.getColor(context, R.color.white)
            } else {
                ContextCompat.getColor(context, R.color.black)
            }
            val backgroundColor = if (isFilled) {
                R.drawable.primary_circle_border
            } else {
                R.drawable.circle_border
            }
            binding.tvNumber.setTextColor(color)
            binding.tvNumber.setBackgroundResource(backgroundColor)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LatContentItemItem>() {
            override fun areItemsTheSame(oldItem: LatContentItemItem, newItem: LatContentItemItem): Boolean {
                return oldItem.question == newItem.question
            }

            override fun areContentsTheSame(oldItem: LatContentItemItem, newItem: LatContentItemItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
