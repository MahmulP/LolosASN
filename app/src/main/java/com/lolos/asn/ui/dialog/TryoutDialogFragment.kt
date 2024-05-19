package com.lolos.asn.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lolos.asn.databinding.FragmentTryoutDialogBinding

class TryoutDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentTryoutDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTryoutDialogBinding.inflate(inflater, container, false)

        binding.cvPremium.setOnClickListener {
            Toast.makeText(requireContext(), "CardView premium", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.cvFree.setOnClickListener {
            Toast.makeText(requireContext(), "Cardview free", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return binding.root
    }
}