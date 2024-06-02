package com.lolos.asn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

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

//        authViewModel.getAuthUser().observe(viewLifecycleOwner) {
//
//            if (it.userId != null) {
//                val userId = it.userId
//                authViewModel.getAUthUserData(userId)
//            }
//        }

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
    }
}