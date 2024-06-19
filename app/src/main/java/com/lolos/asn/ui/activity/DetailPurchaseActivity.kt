package com.lolos.asn.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.PurchaseResponse
import com.lolos.asn.data.retrofit.ApiConfig
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.ActivityDetailPurchaseBinding
import com.lolos.asn.ui.dialog.PaymentStatusDialogFragment
import com.lolos.asn.utils.reduceFileImage
import com.lolos.asn.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class DetailPurchaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPurchaseBinding

    private var currentImageUri: Uri? = null
    private var title: String? = null
    private var price: Int? = null
    private var listTryout: List<String?>? = null
    private var userId: String? = null
    private var token: String = "token"

    val tryoutViewModel by viewModels<TryoutViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val bundleId = intent.getStringExtra("bundle_id")

        authViewModel.getAuthUser().observe(this) {
            if (it?.token != null) {
                userId = it.userId
                token = "Bearer ${it.token}"
                tryoutViewModel.getBundleTryoutDetail(userId = userId, bundleId = bundleId, token = token)
            }
        }

        tryoutViewModel.bundleTryoutDetail.observe(this) { bundleDetail ->
            if (bundleDetail != null) {
                val basePrice = bundleDetail.data.price.toInt()
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

                title = bundleDetail.data.tryoutBundleName
                price = bundleDetail.data.basePrice
                listTryout = bundleDetail.data.listTryoutId

            }
        }

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSend.setOnClickListener {
            if (currentImageUri != null) {
                uploadPurchase()
            } else {
                showToast(getString(R.string.empty_image_warning))
            }
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

    private fun uploadPurchase() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val transactionTitle = title
            val basePrice = price.toString()
            val tryoutData: List<String?>? = listTryout
            showLoading(true)

            val transactionBody = transactionTitle?.toRequestBody("text/plain".toMediaType())
            val priceBody = basePrice.toRequestBody("text/plain".toMediaType())

            // Convert list to JSON string
            val gson = Gson()
            val tryoutDataJson = gson.toJson(tryoutData)
            val tryoutDataBody = tryoutDataJson.toRequestBody("application/json".toMediaType())

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "bukti_transaksi",
                imageFile.name,
                requestImageFile
            )

            val apiService = ApiConfig.getApiService()
            val call = apiService.sendTransaction(
                userId,
                multipartBody,
                transactionBody!!,
                priceBody,
                tryoutDataBody,
                token
            )

            call.enqueue(object : Callback<PurchaseResponse> {
                override fun onResponse(call: Call<PurchaseResponse>, response: Response<PurchaseResponse>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val dialog = PaymentStatusDialogFragment()
                        dialog.show(supportFragmentManager, "PaymentStatusDialog")
                    } else {
                        showToast(getString(R.string.purchase_failed))
                    }
                }

                override fun onFailure(call: Call<PurchaseResponse>, t: Throwable) {
                    showLoading(false)
                    showToast(getString(R.string.purchase_failed))
                    Log.e("Upload Purchase", "onFailure: ${t.message}", t)
                }
            })
        } ?: showToast(getString(R.string.empty_image_warning))
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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