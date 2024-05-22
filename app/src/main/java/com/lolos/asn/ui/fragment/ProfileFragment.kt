package com.lolos.asn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentProfileBinding
import com.lolos.asn.ui.dialog.LogoutDialogFragment

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
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

        binding.cvPrivacy.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "Privacy")
            }
            findNavController().navigate(R.id.action_profile_to_termPrivacyFragment, bundle)
        }
        binding.cvTerms.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", "TermsCondition")
            }
            findNavController().navigate(R.id.action_profile_to_termPrivacyFragment, bundle)
        }
        binding.cvEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Under maintenance", Toast.LENGTH_SHORT).show()
        }
        binding.cvLogout.setOnClickListener {
            val dialog = LogoutDialogFragment()
            dialog.show(parentFragmentManager, "LogoutDialogFragment")
        }
    }
}