package com.lolos.asn.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lolos.asn.R
import com.lolos.asn.databinding.ActivityDetailPurchaseBinding
import com.lolos.asn.ui.dialog.NumberDialogFragment
import com.lolos.asn.ui.dialog.PaymentStatusDialogFragment

class DetailPurchaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPurchaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPurchaseBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val title = intent.getStringExtra("title")

        binding.tvTitle.text = title

        binding.btnSend.setOnClickListener {
            val dialog = PaymentStatusDialogFragment()
            dialog.show(supportFragmentManager, "PaymentStatusDialog")
        }
    }
}