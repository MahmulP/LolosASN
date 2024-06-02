package com.lolos.asn.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lolos.asn.R
import com.lolos.asn.data.preference.IntroductionPreferences
import com.lolos.asn.data.preference.introPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.IntroViewModelFactory
import com.lolos.asn.data.viewmodel.model.IntroViewModel
import com.lolos.asn.utils.isInternetAvailable

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val introViewModel: IntroViewModel by viewModels {
        val pref = IntroductionPreferences.getInstance(application.introPreferencesDataStore)
        IntroViewModelFactory(pref)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isInternetAvailable(this)) {
                introViewModel.getIntroStatus().observe(this) { status ->
                    if (status == true) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, IntroductionActivity::class.java))
                        finish()
                    }
                }
            } else {
                showNoInternetWarning()
            }
        }, 2000)
    }

    private fun showNoInternetWarning() {
        Toast.makeText(this, "No internet connection available", Toast.LENGTH_LONG).show()
    }
}