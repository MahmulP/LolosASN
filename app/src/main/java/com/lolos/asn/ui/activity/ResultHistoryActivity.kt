package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.TryoutHistoryAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.FinishedTryoutResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.ActivityResultHistoryBinding

class ResultHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultHistoryBinding
    private val tryoutViewModel by viewModels<TryoutViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(application.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var userId: String? = null
    private var token: String = "token"
    private var transactionFrom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionFrom = intent.getStringExtra("transactionFrom")

        Log.d("LAPETGADUNG", "onCreate: $transactionFrom")

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        authViewModel.getAuthUser().observe(this) {
            if (it.userId != null && it.token != null) {
                userId = it.userId
                token = "Bearer ${it.token}"
                tryoutViewModel.getFinishedTryout(userId, token)
            }
        }

        tryoutViewModel.finishedTryout.observe(this) { finishedTryout ->
            if (finishedTryout != null) {
                setupRecycleView(finishedTryout)
            }
        }

        tryoutViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
            Log.d("CheckLoading", "onCreate: $isLoading")
        }

        tryoutViewModel.isEmpty.observe(this) { isEmpty ->
            showEmpty(isEmpty)
            Log.d("CheckEmpty", "onCreate: $isEmpty")
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (transactionFrom == "TryoutFragment") {
                    remove()
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    val intent = Intent(this@ResultHistoryActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
            }
        })

    }

    private fun showEmpty(isEmpty: Boolean) {
        binding.ivEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecycleView(finishedTryout: FinishedTryoutResponse) {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        val adapter = TryoutHistoryAdapter(this)
        binding.rvHistory.adapter = adapter

        finishedTryout.data.let {
            adapter.submitList(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (transactionFrom == "TryoutFragment") {
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    val intent = Intent(this@ResultHistoryActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}