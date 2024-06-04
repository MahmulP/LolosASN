package com.lolos.asn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.ListTopNumberBinding
import com.lolos.asn.ui.activity.ExaminationActivity

class ExaminationNumberAdapter(
    private val parentActivity: ExaminationActivity,
    private val viewModel: ExaminationViewModel
) : ListAdapter<TryoutContentItem, ExaminationNumberAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListTopNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, viewModel, parentActivity)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val examination = getItem(position)
        holder.bind(examination, position)

        holder.binding.tvNumber.setOnClickListener {
            parentActivity.updateQuestion(position)
            viewModel.updatePosition(position)
        }
    }

    class MyViewHolder(
        val binding: ListTopNumberBinding,
        private val viewModel: ExaminationViewModel,
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

        fun bind(examination: TryoutContentItem, position: Int) {
            binding.tvNumber.text = (position + 1).toString()
            val isFilled = viewModel.isAnswerFilled(position)
            updateBackgroundTint(isFilled)
        }

        private fun updateBackgroundTint(isFilled: Boolean) {
            val context = binding.root.context
            val color = if (isFilled) {
                getColor(context, R.color.white)
            } else {
                getColor(context, R.color.black)
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TryoutContentItem>() {
            override fun areItemsTheSame(oldItem: TryoutContentItem, newItem: TryoutContentItem): Boolean {
                return oldItem.question == newItem.question
            }

            override fun areContentsTheSame(oldItem: TryoutContentItem, newItem: TryoutContentItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
