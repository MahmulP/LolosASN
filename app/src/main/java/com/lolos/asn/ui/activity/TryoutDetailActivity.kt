package com.lolos.asn.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lolos.asn.R
import com.lolos.asn.databinding.ActivityTryoutDetailBinding
import com.lolos.asn.ui.dialog.StartDialogFragment

class TryoutDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTryoutDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTryoutDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnStart.setOnClickListener {
            val dialog = StartDialogFragment()
            dialog.show(supportFragmentManager, "CustomDialog")
        }
    }
}