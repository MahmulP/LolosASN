package com.lolos.asn.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.lolos.asn.R
import com.lolos.asn.databinding.ActivityExaminationBinding
import com.lolos.asn.ui.dialog.NumberDialogFragment

class ExaminationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExaminationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExaminationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cardA = binding.opsiA
        val cardB = binding.opsiB
        val cardC = binding.opsiC
        val cardD = binding.opsiD

        val cards = listOf(cardA, cardB, cardC, cardD)

        cards.forEach { card ->
            card.setOnClickListener {
                cards.forEach { it.isChecked = false }
                card.isChecked = true
            }
        }

        binding.btnCheckAll.setOnClickListener {
            val dialog = NumberDialogFragment()
            dialog.show(supportFragmentManager, "NumberDialogFragment")
        }
    }
}