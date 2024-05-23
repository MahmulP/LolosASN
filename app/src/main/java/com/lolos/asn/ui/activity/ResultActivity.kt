package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lolos.asn.R
import com.lolos.asn.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvDetailRank.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        binding.tvDetailPembahasan.setOnClickListener {
            startActivity(Intent(this, ResultDiscussionActivity::class.java))
        }

        binding.tvDetailAi.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        binding.btnDone.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigate_to", "learning")
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("navigate_to", "learning")
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}