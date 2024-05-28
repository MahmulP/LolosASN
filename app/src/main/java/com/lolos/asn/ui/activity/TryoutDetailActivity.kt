package com.lolos.asn.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutDetailViewModel
import com.lolos.asn.databinding.ActivityTryoutDetailBinding
import com.lolos.asn.ui.dialog.StartDialogFragment
import java.text.SimpleDateFormat
import java.util.Locale

class TryoutDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTryoutDetailBinding
    private val tryoutDetailViewModel by viewModels<TryoutDetailViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTryoutDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tryoutId = intent.getStringExtra("tryout_id")
        val period = intent.getStringExtra("tryout_period")

        authViewModel.getAuthUser().observe(this) {
            val userId = it.userId
            tryoutDetailViewModel.getTryoutDetail(tryoutId, userId)
        }

        tryoutDetailViewModel.tryout.observe(this) {
            binding.tvTitle.text = it.data?.tryoutTitle
            binding.tvDateRange.text = period
        }

        binding.btnStart.setOnClickListener {
            val dialog = StartDialogFragment.newInstance(tryoutId)
            dialog.show(supportFragmentManager, "CustomDialog")
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