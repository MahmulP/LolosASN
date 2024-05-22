package com.lolos.asn.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.lolos.asn.databinding.FragmentLogoutDialogBinding
import com.lolos.asn.databinding.FragmentStartDialogBinding
import com.lolos.asn.ui.activity.ExaminationActivity
import com.lolos.asn.ui.activity.LoginActivity

class LogoutDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentLogoutDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            Toast.makeText(requireContext(), "Anda berhasil keluar", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}