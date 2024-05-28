package com.lolos.asn.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.LeaderboardAdapter
import com.lolos.asn.data.response.LeaderboardResponse
import com.lolos.asn.data.viewmodel.model.LeaderboardViewModel
import com.lolos.asn.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private val leaderboardViewModel by viewModels<LeaderboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tryoutId = "1"
        leaderboardViewModel.getLeaderboardData(tryoutId)

        leaderboardViewModel.leaderboardData.observe(this) { leaderboard ->
            setupRecycleView(leaderboard)
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