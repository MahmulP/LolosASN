package com.lolos.asn.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lolos.asn.R
import com.lolos.asn.databinding.ActivityResultHistoryBinding

class ResultHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}