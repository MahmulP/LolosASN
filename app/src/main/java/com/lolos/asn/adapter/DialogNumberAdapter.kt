package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.ListDialogNumberBinding
import com.lolos.asn.ui.dialog.NumberDialogFragment

class DialogNumberAdapter(
    private val viewModel: ExaminationViewModel,
    private val dialogFragment: NumberDialogFragment
) : ListAdapter<TryoutContentItem, DialogNumberAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListDialogNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, viewModel, dialogFragment)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val examination = getItem(position)
        holder.bind(examination, position)

        if (position % 5 == 4) {
            setMargins(holder.binding.btnNumber, 0, 16, 0, 0)
        } else {
            setMargins(holder.binding.btnNumber, 0, 16, 8, 0)
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    class MyViewHolder(
        val binding: ListDialogNumberBinding,
        private val viewModel: ExaminationViewModel,
        private val dialogFragment: NumberDialogFragment
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var currentExamination: TryoutContentItem

        init {
            binding.btnNumber.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    viewModel.updatePosition(currentExamination.tryoutNum.toInt() - 1)
                    dialogFragment.dismiss()
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(examination: TryoutContentItem, position: Int) {
            currentExamination = examination
            binding.btnNumber.text = (position + 1).toString()
            val isFilled = viewModel.isAnswerFilled(examination.tryoutNum.toInt() - 1)
            updateBackgroundTint(isFilled)
        }

        private fun updateBackgroundTint(isFilled: Boolean) {
            val context = binding.root.context
            val textColor = if (isFilled) {
                ContextCompat.getColor(context, R.color.white)
            } else {
                ContextCompat.getColor(context, R.color.primaryColor)
            }
            val backgroundColorTint = if (isFilled) {
                ContextCompat.getColorStateList(context, R.color.primaryColor)
            } else {
                ContextCompat.getColorStateList(context, R.color.thirdPrimary)
            }
            binding.btnNumber.setTextColor(textColor)
            binding.btnNumber.backgroundTintList = backgroundColorTint
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
