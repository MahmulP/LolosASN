package com.lolos.asn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.ListChoiceBinding
import com.lolos.asn.ui.activity.ExaminationActivity

class ExaminationAnswerAdapter(
    private val parentActivity: ExaminationActivity,
    private val viewModel: ExaminationViewModel
) : ListAdapter<String, ExaminationAnswerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var options: List<String?> = emptyList()
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListChoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(options[position], position)
        holder.itemView.setOnClickListener {
            updateSelection(position)
            viewModel.selectAnswer(viewModel.currentQuestionIndex.value ?: 0, position)
            viewModel.calculateScores(viewModel.currentQuestionIndex.value ?: 0, position)
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }

    fun setOptions(newOptions: List<String?>, selectedAnswer: Int?) {
        options = newOptions
        selectedPosition = selectedAnswer ?: -1
        notifyDataSetChanged()
    }

    private fun updateSelection(position: Int) {
        if (position != selectedPosition) {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
        }
    }

    inner class MyViewHolder(val binding: ListChoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: String?, position: Int) {
            binding.tvChoiceText.text = option
            binding.tvChoice.text = getLetterForPosition(position)
            binding.cvOption.isChecked = position == selectedPosition
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