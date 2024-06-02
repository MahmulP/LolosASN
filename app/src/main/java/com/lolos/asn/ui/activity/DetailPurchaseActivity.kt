package com.lolos.asn.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.ActivityDetailPurchaseBinding
import com.lolos.asn.ui.dialog.NumberDialogFragment
import com.lolos.asn.ui.dialog.PaymentStatusDialogFragment
import java.text.NumberFormat
import java.util.Locale

class DetailPurchaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPurchaseBinding

    private var currentImageUri: Uri? = null

    val tryoutViewModel by viewModels<TryoutViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.main)

        val bundleId = intent.getStringExtra("bundle_id")

        authViewModel.getAuthUser().observe(this) {
            if (it != null) {
                val userId = it.userId
                tryoutViewModel.getBundleTryoutDetail(userId = userId, bundleId = bundleId)
            }
        }

        tryoutViewModel.bundleTryoutDetail.observe(this) { bundleDetail ->
            if (bundleDetail != null) {
                val basePrice = bundleDetail.data.basePrice
                val totalPrice = basePrice + 0
                val indonesianLocale = Locale("in", "ID")
                val formattedBasePrice = NumberFormat.getNumberInstance(indonesianLocale).format(basePrice)
                val formattedTotalPrice = NumberFormat.getNumberInstance(indonesianLocale).format(totalPrice)

                with(binding) {
                    tvTitle.text = bundleDetail.data.tryoutBundleName
                    tvPrice.text = getString(R.string.price, formattedBasePrice)
                    tvInformationPrice.text = getString(R.string.price, formattedBasePrice)
                    tvAdminPrice.text = getString(R.string.price, "0")
                    tvTotalPrice.text = getString(R.string.price, formattedTotalPrice)
                }
            }
        }

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSend.setOnClickListener {
            val dialog = PaymentStatusDialogFragment()
            dialog.show(supportFragmentManager, "PaymentStatusDialog")
        }

        binding.ibUpload.setOnClickListener {
            startGallery()
        }

        binding.ibClose.setOnClickListener {
            currentImageUri = null
            binding.ivPicture.visibility = View.GONE
            binding.ibUpload.visibility = View.VISIBLE
            binding.ibClose.visibility = View.GONE
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

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            binding.ivPicture.setImageURI(currentImageUri)
            binding.ivPicture.visibility = View.VISIBLE
            binding.ibUpload.visibility = View.GONE
            binding.ibClose.visibility = View.VISIBLE
            Log.d(TAG, currentImageUri.toString())
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    companion object {
        private const val TAG = "DetailPurchaseActivity"
    }
}