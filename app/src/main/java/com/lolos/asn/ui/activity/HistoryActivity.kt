package com.lolos.asn.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.TransactionHistoryAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.TransactionHistoryResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TransactionViewModel
import com.lolos.asn.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val transactionViewModel by viewModels<TransactionViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(application.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnDone.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigate_to", "main")
            startActivity(intent)
        }

        authViewModel.getAuthUser().observe(this) { userData ->
            if (userData?.token != null) {
                val token = "Bearer ${userData.token}"
                transactionViewModel.getTransactionHistory(userData.userId, token)
            }
        }

        transactionViewModel.transactionHistory.observe(this) { transaction->
            setupRecycleView(transaction)
        }

        transactionViewModel.isEmpty.observe(this) {
            showEmpty(it)
        }

        transactionViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupRecycleView(transactionHistoryResponse: TransactionHistoryResponse?) {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        val adapter = TransactionHistoryAdapter(this)
        binding.rvHistory.adapter = adapter

        transactionHistoryResponse?.data.let {
            adapter.submitList(it)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intentFrom = intent.getStringExtra("intentFrom")
                if (intentFrom == "notification") {
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("navigate_to", "main")
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}