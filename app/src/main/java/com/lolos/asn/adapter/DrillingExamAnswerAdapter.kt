package com.lolos.asn.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.viewmodel.model.DrillingStartViewModel
import com.lolos.asn.databinding.ListChoiceBinding
import com.lolos.asn.ui.activity.DrillingExamActivity

class DrillingExamAnswerAdapter(
    private val parentActivity: DrillingExamActivity,
    private val viewModel: DrillingStartViewModel
) : ListAdapter<String, DrillingExamAnswerAdapter.MyViewHolder>(DIFF_CALLBACK) {

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
            viewModel.selectAnswer(viewModel.questionHandicapIndex.value ?: 0, position)
            viewModel.calculateScores(viewModel.currentQuestionIndex.value ?: 0, viewModel.questionHandicapIndex.value ?: 0, position)
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
            val white = ContextCompat.getColor(binding.root.context, R.color.white)
            val black = ContextCompat.getColor(binding.root.context, R.color.black)
            val primaryColor = ContextCompat.getColor(binding.root.context, R.color.primaryColor)
            val otherPrimary = ContextCompat.getColor(binding.root.context, R.color.otherPrimary)
            val typeface = ResourcesCompat.getFont(binding.root.context, R.font.roboto_bold)
            val normalFont = ResourcesCompat.getFont(binding.root.context, R.font.roboto)

            binding.tvChoiceText.text = option
            binding.tvChoice.text = getLetterForPosition(position)
            binding.cvOption.isChecked = position == selectedPosition
            if (binding.cvOption.isChecked) {
                binding.cvOption.backgroundTintList = ColorStateList.valueOf(otherPrimary)
                binding.tvChoice.backgroundTintList = ColorStateList.valueOf(white)
                binding.tvChoice.setTextColor(primaryColor)
                binding.tvChoice.typeface = typeface
                binding.cvOption.isChecked = !binding.cvOption.isChecked
            } else {
                binding.cvOption.backgroundTintList = ColorStateList.valueOf(white)
                binding.tvChoice.backgroundTintList = ColorStateList.valueOf(otherPrimary)
                binding.tvChoice.setTextColor(black)
                binding.tvChoice.typeface = normalFont
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