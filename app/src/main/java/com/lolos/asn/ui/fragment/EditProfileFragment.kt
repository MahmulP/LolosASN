package com.lolos.asn.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.databinding.FragmentEditProfileBinding
import com.lolos.asn.utils.reduceFileImage
import com.lolos.asn.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var currentImageUri: Uri? = null
    private var userId: String? = null
    private var name: String? = null
    private var email: String? = null
    private var password: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        authViewModel.getAuthUser().observe(viewLifecycleOwner) {
            if (it.userId != null) {
                userId = it.userId
            }
        }

        authViewModel.getUserData().observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                val avatar = userData.avatar

                binding.edName.setText(userData.name)
                binding.edEmail.setText(userData.email)
                Glide.with(this)
                    .load(avatar)
                    .error(R.drawable.avatar)
                    .into(binding.ivUserAvatar)
            }
        }

        authViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it != null) {
                showLoading(it)
            }
        }

        authViewModel.isChanged.observe(viewLifecycleOwner) { isChanged ->
            if (isChanged == true) {
                findNavController().navigate(R.id.menu_profil)
            }
        }

        binding.btnChangeAvatar.setOnClickListener {
            startGallery()
        }

        binding.btnSave.setOnClickListener {
            updateData()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun updateData() {
        name = binding.edName.text.toString()
        password = binding.edPassword.text.toString()
        email = binding.edEmail.text.toString()

        val nameBody = name?.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = password?.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email?.toRequestBody("text/plain".toMediaTypeOrNull())

        if (currentImageUri != null) {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, requireActivity()).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")

                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Part.createFormData(
                    "avatar",
                    imageFile.name,
                    requestImageFile
                )

                authViewModel.updateUserData(
                    userId = userId,
                    name = nameBody,
                    password = passwordBody,
                    email = emailBody,
                    avatar = multipartBody
                )
            }
        } else {
            authViewModel.updateUserData(
                userId = userId,
                name = nameBody,
                password = passwordBody,
                email = emailBody,
                avatar = null
            )
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            binding.ivUserAvatar.setImageURI(uri)
            Log.d(TAG, currentImageUri.toString())
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    companion object {
        private const val TAG = "EditProfileFragment"
    }
}