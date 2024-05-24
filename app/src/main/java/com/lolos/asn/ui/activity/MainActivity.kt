package com.lolos.asn.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.databinding.ActivityMainBinding
import com.lolos.asn.ui.dialog.TryoutDialogFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(application.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel.getAuthUser().observe(this) { authUser ->
            if (authUser.isNullOrBlank() && authUser == "") {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.bottomNavigation
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            val currentDestination = navController.currentDestination?.id

            when (item.itemId) {
                R.id.menu_home -> {
                    if (currentDestination != R.id.menu_home) {
                        navController.navigate(R.id.menu_home)
                    }
                    true
                }
                R.id.menu_tryout -> {
                    if (currentDestination != R.id.menu_tryout) {
                        val dialog = TryoutDialogFragment()
                        dialog.show(supportFragmentManager, "TryoutDialogFragment")
                    }
                    true
                }
                R.id.menu_materi -> {
                    if (currentDestination != R.id.menu_materi) {
                        navController.navigate(R.id.menu_materi)
                    }
                    true
                }
                R.id.menu_profil -> {
                    if (currentDestination != R.id.menu_profil && currentDestination != R.id.termPrivacyFragment) {
                        navController.navigate(R.id.menu_profil)
                    }
                    true
                }
                else -> false
            }
        }

        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo != null) {
            when (navigateTo) {
                "learning" -> {
                    navController.navigate(R.id.menu_materi)
                }
                "main" -> {
                    navController.navigate(R.id.menu_home)
                }
            }
        }
    }
}