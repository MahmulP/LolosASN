package com.lolos.asn.ui.activity

import android.os.Build
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.databinding.ActivityAnalysisBinding

class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(application.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var username: String? = null
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.getAuthUser().observe(this) { userData ->
            userId = userData.userId
        }

        authViewModel.getUserData().observe(this) { userData ->
            username = userData.name
            binding.tvGreet.text = getString(R.string.greet, username)
        }

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tvContent.justificationMode = JUSTIFICATION_MODE_INTER_WORD
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