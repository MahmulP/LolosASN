package com.lolos.asn.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentTryoutDialogBinding

class TryoutDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentTryoutDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTryoutDialogBinding.inflate(inflater, container, false)

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