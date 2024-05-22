package com.lolos.asn.ui.dialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentValidationPurchaseDialogBinding
import com.lolos.asn.ui.activity.DetailPurchaseActivity

class ValidationPurchaseDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentValidationPurchaseDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValidationPurchaseDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString("title")

        binding.tvTitle.text = title

        binding.btnBuy.setOnClickListener {
            val intent = Intent(requireContext(), DetailPurchaseActivity::class.java)
            intent.putExtra("title", title)
            startActivity(intent)
            dismiss()
        }
    }

    companion object {
        fun newInstance(title: String): ValidationPurchaseDialogFragment {
            val fragment = ValidationPurchaseDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }
}