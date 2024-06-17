package com.lolos.asn.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lolos.asn.R
import com.lolos.asn.adapter.AnalysisSectionsPageAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AnalysisViewModel
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.databinding.ActivityAnalysisBinding

class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding

    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(application.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    private val analysisViewModel by viewModels<AnalysisViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tryoutId = intent.getStringExtra("tryout_id")

        authViewModel.getAuthUser().observe(this) { userData ->
            val userId = userData.userId
            val token = "Bearer ${userData.token}"
            analysisViewModel.getAnalysis(tryoutId = tryoutId, userId = userId, token = token)
        }

        analysisViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val analysisSectionsPagerAdapter = AnalysisSectionsPageAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = analysisSectionsPagerAdapter

        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabTextView = tab.customView?.findViewById<TextView>(R.id.tabTextView)
            tabTextView?.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            viewPager.setCurrentItem(currentItem + 1, true)
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
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

    companion object {

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_all,
            R.string.tab_twk,
            R.string.tab_tiu,
            R.string.tab_tkp
        )
    }
}