package com.lolos.asn.ui.activity

import android.content.Intent
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

class TryoutDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTryoutDetailBinding
    private val tryoutDetailViewModel by viewModels<TryoutDetailViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var tryoutName: String? = null

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

        tryoutDetailViewModel.tryout.observe(this) { tryoutResponse ->
            val isAccessed = tryoutResponse.data?.accessed
            val isCleared = tryoutResponse.data?.isCleared
            val type = tryoutResponse.data?.tryoutType

            tryoutName = tryoutResponse.data?.tryoutTitle
            binding.tvTitle.text = tryoutResponse.data?.tryoutTitle
            binding.tvDateRange.text = period

            if (type == "FREE") {
                if (isCleared == "1") {
                    binding.btnStart.text = getString(R.string.result)
                    binding.btnStart.setOnClickListener {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra("tryout_id", tryoutResponse.data.tryoutId)
                        startActivity(intent)
                    }
                } else {
                    binding.btnStart.setOnClickListener {
                        val dialog = StartDialogFragment.newInstance(tryoutId, tryoutName)
                        dialog.show(supportFragmentManager, "CustomDialog")
                    }
                }
            } else {
                if (isAccessed == "1" && isCleared == "1") {
                    binding.btnStart.text = getString(R.string.result)
                    binding.btnStart.setOnClickListener {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra("tryout_id", tryoutResponse.data.tryoutId)
                        startActivity(intent)
                    }
                } else if (isAccessed == "1") {
                    binding.btnStart.setOnClickListener {
                        val dialog = StartDialogFragment.newInstance(tryoutId, tryoutName)
                        dialog.show(supportFragmentManager, "CustomDialog")
                    }
                } else {
                    binding.btnStart.text = getString(R.string.buy_now)
                    binding.btnStart.setOnClickListener {
                        startActivity(Intent(this, PurchaseActivity::class.java))
                    }
                }
            }
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