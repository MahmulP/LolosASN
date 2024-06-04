package com.lolos.asn.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.factory.TryoutViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.FragmentTryoutDialogBinding

class TryoutDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentTryoutDialogBinding
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    private val tryoutViewModel: TryoutViewModel by viewModels {
        TryoutViewModelFactory(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTryoutDialogBinding.inflate(inflater, container, false)

        authViewModel.getAuthUser().observe(viewLifecycleOwner) { userData ->
            if (userData.userId != null) {
                tryoutViewModel.getPaidTryout(userData.userId)
                tryoutViewModel.getFreeTryout(userData.userId)
            }
        }

        tryoutViewModel.paidTryout.observe(viewLifecycleOwner) {
            val total = it?.data?.size.toString()
            binding.tvPacket.text = "$total Paket Tryout"
        }

        tryoutViewModel.freeTryout.observe(viewLifecycleOwner) {
            val total = it?.data?.size.toString()
            binding.tvFreePacket.text = "$total Paket Tryout"
        }

        binding.cvPremium.setOnClickListener {
            val bundle = Bundle().apply {
                putString("typeTryout", "Premium")
            }
            findNavController().navigate(R.id.action_global_to_tryout, bundle)
            dismiss()
        }

        binding.cvFree.setOnClickListener {
            val bundle = Bundle().apply {
                putString("typeTryout", "Gratis")
            }
            findNavController().navigate(R.id.action_global_to_tryout, bundle)
            dismiss()
        }

        return binding.root
    }
}