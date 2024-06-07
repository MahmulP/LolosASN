package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.lolos.asn.databinding.ActivityNoConnectionBinding
import com.lolos.asn.utils.isInternetAvailable

class NoConnectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoConnectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoConnectionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnRetry.setOnClickListener {
            if (isInternetAvailable(this)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Internet masih belum terdeteksi", Toast.LENGTH_LONG).show()
            }
        }
    }
}