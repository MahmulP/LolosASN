package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lolos.asn.data.preference.IntroductionPreferences
import com.lolos.asn.data.preference.introPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.IntroViewModelFactory
import com.lolos.asn.data.viewmodel.model.IntroViewModel
import com.lolos.asn.databinding.ActivityIntroductionBinding

class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroductionBinding
    private val introViewModel: IntroViewModel by viewModels {
        val pref = IntroductionPreferences.getInstance(application.introPreferencesDataStore)
        IntroViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        introViewModel.getIntroStatus().observe(this) { status ->
            if (status == true) {
                startActivity(Intent(this@IntroductionActivity, MainActivity::class.java))
            }
        }

        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.skipText.setOnClickListener {
            introViewModel.changeStatus(true)
        }

        binding.btnDone.setOnClickListener {
            introViewModel.changeStatus(true)
        }
    }
}