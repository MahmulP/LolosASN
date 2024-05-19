package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lolos.asn.R
import com.lolos.asn.databinding.ActivityIntroductionBinding

class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroductionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.skipText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnDone.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}