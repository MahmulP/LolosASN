package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.ArticlePopularAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.PopularArticleResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.ArticleViewModel
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.databinding.ActivityArticleBinding
import com.lolos.asn.utils.MarginItemDecoration

class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding

    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    private val articleViewModel by viewModels<ArticleViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        authViewModel.getAuthUser().observe(this) {
            val token = "Bearer ${it.token}"
            articleViewModel.getPopularArticle(token)
        }

        articleViewModel.popularArticle.observe(this) {
            setupPopularArticleRecycleView(it)
        }

        binding.cvNewestArticle.setOnClickListener {
            startActivity(Intent(this, ArticleDetailActivity::class.java))
        }
    }

    private fun setupPopularArticleRecycleView(popularArticleResponses: List<PopularArticleResponse>?) {
        binding.rvArticlePopular.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Set up the adapter
        val adapter = ArticlePopularAdapter(this)
        val density = this.resources.displayMetrics.density
        val smallestScreenWidthDp = resources.configuration.smallestScreenWidthDp

        val firstItemStartMargin: Int
        val lastItemEndMargin: Int
        val restItemStartMargin = (16 * density).toInt() // This remains constant

        if (smallestScreenWidthDp >= 420) {
            firstItemStartMargin = (32 * density).toInt()
            lastItemEndMargin = (32 * density).toInt()
        } else if (smallestScreenWidthDp >= 320) {
            firstItemStartMargin = (16 * density).toInt()
            lastItemEndMargin = (16 * density).toInt()
        } else {
            firstItemStartMargin = (32 * density).toInt()
            lastItemEndMargin = (32 * density).toInt()
        }

        val itemDecoration = MarginItemDecoration(firstItemStartMargin, restItemStartMargin, lastItemEndMargin)

        // Remove all item decorations to avoid duplicates
        val itemDecorationCount = binding.rvArticlePopular.itemDecorationCount
        for (i in 0 until itemDecorationCount) {
            binding.rvArticlePopular.removeItemDecorationAt(0)
        }

        binding.rvArticlePopular.addItemDecoration(itemDecoration)
        binding.rvArticlePopular.adapter = adapter

        // Submit the list to the adapter
        popularArticleResponses?.let {
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