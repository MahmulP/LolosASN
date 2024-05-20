package com.lolos.asn.ui.dialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentStartDialogBinding
import com.lolos.asn.databinding.FragmentValidationDialogBinding
import com.lolos.asn.ui.activity.ExaminationActivity
import com.lolos.asn.ui.activity.ResultActivity

class ValidationDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentValidationDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValidationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDone.setOnClickListener {
            startActivity(Intent(requireContext(), ResultActivity::class.java))
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}