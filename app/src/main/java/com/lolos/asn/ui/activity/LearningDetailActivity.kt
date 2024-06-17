package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.CourseDetailViewModel
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

        authViewModel.getAuthUser().observe(this) {
            val userId = it.userId
            val token = "Bearer ${it.token}"
            if (userId != null) {
                courseDetailViewModel.getDetailCourse(courseId, userId, token)
            }
        }

        courseDetailViewModel.courseDetail.observe(this) {
            toolbar.title = it?.data?.courseName
            val itemPicture = it?.data?.courseFile
            val imageView: ImageView = binding.ivContent

            Glide.with(this)
                .load(itemPicture)
                .error(R.drawable.no_image)
                .into(imageView)

            if (it?.data?.isCleared != "0") {
                binding.btnDone.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("navigate_to", "learning")
                    startActivity(intent)
                }
            } else {
                binding.btnDone.setOnClickListener {
                    authViewModel.getAuthUser().observe(this) {
                        val userId = it.userId
                        val token = "Bearer ${it.token}"
                        if (userId != null) {
                            courseDetailViewModel.finishCourse(courseId = courseId, userId = userId, token = token)
                        }
                    }

                    courseDetailViewModel.finishCourse.observe(this) { isSuccess ->
                        if (isSuccess) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("navigate_to", "learning")
                            startActivity(intent)
                        } else {
                            showToast()
                        }
                    }
                }
            }
        }

        courseDetailViewModel.isLoading.observe(this) {
            showLoading(it)
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

    private fun showToast() {
        Toast.makeText(this, "Gagal menyelesaikan materi", Toast.LENGTH_SHORT).show()
    }
}