package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.data.response.Course
import com.lolos.asn.databinding.ListCourseBinding
import com.lolos.asn.ui.activity.LearningDetailActivity

class CourseAdapter(private val context: Context) : ListAdapter<Course, CourseAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course, position, context, itemCount)

        if (position > 0) {
            val previousCourse = getItem(position - 1)
            if (previousCourse.isCleared == "1") {
                val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)
                holder.binding.ivLineTop.imageTintList = ColorStateList.valueOf(primaryColor)
            }
        }
    }

    class MyViewHolder(val binding: ListCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(course: Course, position: Int, context: Context, totalItems: Int) {
            binding.tvTitle.text = "${position + 1}. ${course.courseName}"

            val thumbnail = course.courseImage

            Glide.with(itemView)
                .load(thumbnail)
                .error(R.drawable.no_image)
                .into(binding.ivPicture)

            if (position == 0) {
                binding.ivLineTop.visibility = View.GONE
            } else {
                binding.ivLineTop.visibility = View.VISIBLE
            }

            if (position == totalItems - 1) {
                binding.ivLineBottom.visibility = View.GONE
            } else {
                binding.ivLineBottom.visibility = View.VISIBLE
            }

            if (course.isCleared == "1") {
                val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)
                binding.ivDot.imageTintList = ColorStateList.valueOf(primaryColor)
                binding.ivLineBottom.imageTintList = ColorStateList.valueOf(primaryColor)
            }

            binding.tvBook.setOnClickListener {
                val intent = Intent(context, LearningDetailActivity::class.java)
                intent.putExtra("course_id", course.courseId)
                context.startActivity(intent)
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }
        }
    }
}

