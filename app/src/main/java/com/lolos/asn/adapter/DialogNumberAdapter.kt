package com.lolos.asn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
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
            // Last item, set marginEnd to 0dp
            setMargins(holder.binding.btnNumber, 0, 32, 0, 0)
        } else {
            // Regular item, set default marginEnd
            setMargins(holder.binding.btnNumber, 0, 32, 32, 0)
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

        init {
            binding.btnNumber.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    viewModel.updatePosition(position)
                    dialogFragment.dismiss()
                }
            }
        }

        fun bind(examination: TryoutContentItem, position: Int) {
            binding.btnNumber.text = (position + 1).toString()
            val isFilled = viewModel.isAnswerFilled(position)
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
