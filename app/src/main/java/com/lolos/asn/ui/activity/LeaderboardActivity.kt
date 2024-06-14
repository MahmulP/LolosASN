package com.lolos.asn.ui.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.adapter.LeaderboardAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.LeaderboardResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.LeaderboardViewModel
import com.lolos.asn.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private val leaderboardViewModel by viewModels<LeaderboardViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(application.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tryoutId = intent.getStringExtra("tryout_id")

        leaderboardViewModel.leaderboardData.observe(this) { leaderboard ->
            if (leaderboard?.data != null) {
                setupRecycleView(leaderboard)

                authViewModel.getAuthUser().observe(this) { userData ->
                    if (userData.userId != null) {
                        val userId = userData.userId

                        if (userData.token != null) {
                            val token = "Bearer ${userData.token}"
                            leaderboardViewModel.getLeaderboardData(tryoutId, token)
                        }

                        leaderboard.data.forEachIndexed { index, item ->
                            if (item.accountId == userId) {
                                val accountAvatar = item.account?.avatar
                                val fullName = item.account?.name ?: ""
                                val nameParts = fullName.split(" ")
                                val displayName = if (nameParts.size >= 2) {
                                    "${nameParts[0]} ${nameParts[1]}"
                                } else {
                                    fullName
                                }

                                binding.userName.text = displayName
                                binding.tvUserRank.text = "${index + 1}"
                                binding.tvUserPoint.text = item.tryoutScore.toString()
                                Glide.with(binding.ivUser.context)
                                    .load(accountAvatar)
                                    .error(R.drawable.avatar)
                                    .into(binding.ivUser)

                                if (item.tryoutPassed != "Passed") {
                                    val colorRed100 = ContextCompat.getColor(this, R.color.red_100)
                                    val drawable = ContextCompat.getDrawable(this, R.drawable.person_cry)

                                    binding.cvAuthUser.backgroundTintList = ColorStateList.valueOf(colorRed100)
                                    binding.tvUserPoint.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)))
                                    binding.ivTrophy.setImageDrawable(drawable)
                                }

                                return@forEachIndexed
                            }
                        }
                    }
                }
            }
        }

        leaderboardViewModel.isEmpty.observe(this) {
            showEmpty(it)
        }

        leaderboardViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showEmpty(status: Boolean) {
        if (status) {
            binding.ivEmpty.visibility = View.VISIBLE
        } else {
            binding.ivEmpty.visibility = View.GONE
        }
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupRecycleView(leaderboardResponse: LeaderboardResponse?){
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(this)

        // Set up the adapter
        val adapter = LeaderboardAdapter(this)
        binding.rvLeaderboard.adapter = adapter

        // Submit the list to the adapter
        leaderboardResponse?.data.let {
            adapter.submitList(it)
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
}