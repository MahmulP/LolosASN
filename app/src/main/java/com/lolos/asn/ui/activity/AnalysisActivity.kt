package com.lolos.asn.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lolos.asn.R
import com.lolos.asn.adapter.AnalysisSectionsPageAdapter
import com.lolos.asn.adapter.LearningSectionsPagerAdapter
import com.lolos.asn.databinding.ActivityAnalysisBinding
import com.lolos.asn.ui.fragment.LearningFragment

class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

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