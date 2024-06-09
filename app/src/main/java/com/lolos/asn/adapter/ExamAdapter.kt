package com.lolos.asn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.ExaminationDisplayItem
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.ListDialogNumberBinding
import com.lolos.asn.databinding.ListHeaderNumberBinding
import com.lolos.asn.ui.dialog.NumberDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExamAdapter(private val dialogFragment: NumberDialogFragment, private val viewModel: ExaminationViewModel) : ListAdapter<ExaminationDisplayItem, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ExaminationDisplayItem.Header -> VIEW_TYPE_HEADER
            is ExaminationDisplayItem.ExaminationItem -> VIEW_TYPE_CATEGORY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ListHeaderNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_CATEGORY -> {
                val binding = ListDialogNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExaminationViewHolder(binding, viewModel, dialogFragment)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ExaminationDisplayItem.Header -> (holder as HeaderViewHolder).bind(item.title)
            is ExaminationDisplayItem.ExaminationItem -> {
                (holder as ExaminationViewHolder).bind(
                    item.examination,
                    dialogFragment,
                    viewModel,
                    position
                )

                if (position % 5 == 4) {
                    setMargins(holder.binding.btnNumber, 0, 32, 0, 0)
                } else {
                    setMargins(holder.binding.btnNumber, 0, 32, 32, 0)
                }
            }
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    class HeaderViewHolder(val binding: ListHeaderNumberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.tvHeaderTitle.text = title
        }
    }

    class ExaminationViewHolder(
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

        fun bind(examination: TryoutContentItem, dialogFragment: NumberDialogFragment, viewModel: ExaminationViewModel, position: Int) {
            with(binding) {
                btnNumber.text = (position + 1).toString()
                val isFilled = viewModel.isAnswerFilled(position)
                updateBackgroundTint(isFilled)
            }
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

    fun setData(items: List<ExaminationDisplayItem>) {
        val headers = items.filterIsInstance<ExaminationDisplayItem.Header>()
        val displayItems = items.filterIsInstance<ExaminationDisplayItem.ExaminationItem>()

        val sortedDisplayItems = displayItems.sortedWith(compareByDescending {
            it.examination.category.let { category ->
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(category.toString())
            } ?: Date(0)
        })

        val mergedList = mutableListOf<ExaminationDisplayItem>()
        var displayItemIndex = 0

        items.forEach { item ->
            when (item) {
                is ExaminationDisplayItem.Header -> mergedList.add(item)
                is ExaminationDisplayItem.ExaminationItem -> mergedList.add(sortedDisplayItems[displayItemIndex++])
            }
        }

        submitList(mergedList)
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_CATEGORY = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExaminationDisplayItem>() {
            override fun areItemsTheSame(oldItem: ExaminationDisplayItem, newItem: ExaminationDisplayItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ExaminationDisplayItem, newItem: ExaminationDisplayItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
