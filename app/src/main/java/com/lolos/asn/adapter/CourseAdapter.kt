package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.data.response.Course
import com.lolos.asn.databinding.ListCourseBinding
import com.lolos.asn.ui.activity.LearningDetailActivity

class CourseAdapter(private val context: Context): ListAdapter<Course, CourseAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course, position, context)
    }

    class MyViewHolder(val binding: ListCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(course: Course, position: Int, context: Context) {
            binding.tvTitle.text = "${position + 1}. ${course.courseName}"

            val thumbnail = course.courseImage

            Glide.with(itemView)
                .load(thumbnail)
                .error(R.drawable.no_image)
                .into(binding.ivPicture)

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
