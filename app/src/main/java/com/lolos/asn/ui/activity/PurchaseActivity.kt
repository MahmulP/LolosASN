package com.lolos.asn.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.PurchaseAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.TryoutBundleResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.ActivityPurchaseBinding

class PurchaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPurchaseBinding

    val tryoutViewModel by viewModels<TryoutViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(application.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        authViewModel.getAuthUser().observe(this) { userData ->
            if (userData != null) {
                val userId = userData.userId
                tryoutViewModel.getBundleTryout(userId)
            }
        }

        tryoutViewModel.bundleTryout.observe(this) {
            setupRecycleView(it)
        }

    }

    private fun setupRecycleView(tryoutBundleResponse: TryoutBundleResponse) {
        binding.rvBundle.layoutManager = LinearLayoutManager(this)

        val adapter = PurchaseAdapter(this)

        binding.rvBundle.adapter = adapter

        tryoutBundleResponse.data.let {
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