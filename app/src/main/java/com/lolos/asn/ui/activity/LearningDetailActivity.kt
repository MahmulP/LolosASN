package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.CourseDetailViewModel
import com.lolos.asn.data.viewmodel.model.CourseViewModel
import com.lolos.asn.databinding.ActivityLearningDetailBinding

class LearningDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLearningDetailBinding
    private val courseDetailViewModel by viewModels<CourseDetailViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val courseId = intent.getStringExtra("course_id")
        println(courseId)

        authViewModel.getAuthUser().observe(this) {
            val userId = it.userId
            courseDetailViewModel.getDetailCourse(courseId, userId)
        }

        courseDetailViewModel.courseDetail.observe(this) {
            binding.tvTitle.text = it?.data?.courseName
            binding.tvContent.text = it?.data?.content?.filterNotNull()?.joinToString("\n") ?: ""
        }

        courseDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnDone.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}