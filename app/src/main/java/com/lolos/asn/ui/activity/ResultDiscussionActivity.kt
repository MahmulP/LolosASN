package com.lolos.asn.ui.activity

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lolos.asn.R
import com.lolos.asn.databinding.ActivityExaminationBinding
import com.lolos.asn.databinding.ActivityResultDiscussionBinding
import com.lolos.asn.ui.dialog.NumberDialogFragment

class ResultDiscussionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultDiscussionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnCheckAll.setOnClickListener {
            val dialog = NumberDialogFragment()
            dialog.show(supportFragmentManager, "NumberDialogFragment")
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