package com.lolos.asn.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.TokenRequest
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TokenViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.FragmentRedeemTokenDialogBinding

class RedeemTokenDialog : DialogFragment() {
    private lateinit var binding: FragmentRedeemTokenDialogBinding

    private val tokenViewModel by viewModels<TokenViewModel>()
    private val tryoutViewModel: TryoutViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRedeemTokenDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRedeem.setOnClickListener {
            val tokenInput = binding.edRedeem.text.toString()

            authViewModel.getAuthUser().observe(viewLifecycleOwner) { userData ->
                val userId = userData.userId
                val token = "Bearer ${userData.token}"

                tokenViewModel.redeemToken(userId = userId, tokenRequest = TokenRequest(tokenInput), token = token)
            }
        }

        tokenViewModel.isRedeem.observe(viewLifecycleOwner) { isRedeem ->
            showStatus(isRedeem)

            val typeTryout = arguments?.getString(ARG_TYPE_TRYOUT)

            if (isRedeem) {
                if (typeTryout == "Premium") {
                    authViewModel.getAuthUser().observe(viewLifecycleOwner) {userData ->
                        if (userData.userId != null) {
                            val userId = userData.userId
                            val token = "Bearer ${userData.token}"
                            tryoutViewModel.getPaidTryout(userId, token)
                        }
                    }
                } else {
                    authViewModel.getAuthUser().observe(viewLifecycleOwner) {userData ->
                        if (userData.userId != null) {
                            val userId = userData.userId
                            val token = "Bearer ${userData.token}"
                            tryoutViewModel.getFreeTryout(userId, token)
                        }
                    }
                }
            }
        }

        tokenViewModel.isFailed.observe(viewLifecycleOwner) { isFailed ->
            showFailed(isFailed)
        }
    }

    private fun showFailed(failed: Boolean) {
        binding.cvStatusFailed.visibility = if (failed) View.VISIBLE else View.GONE
        binding.edRedeem.isEnabled = !failed

        if (failed) {
            binding.btnRedeem.text = getString(R.string.done_text)
            binding.btnRedeem.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun showStatus(redeem: Boolean) {
        binding.cvStatus.visibility = if (redeem) View.VISIBLE else View.GONE
        binding.edRedeem.isEnabled = !redeem

        if (redeem) {
            binding.btnRedeem.text = getString(R.string.done_text)
            binding.btnRedeem.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        private const val ARG_TYPE_TRYOUT = "typeTryout"

        fun newInstance(typeTryout: String): RedeemTokenDialog {
            val fragment = RedeemTokenDialog()
            val args = Bundle()
            args.putString(ARG_TYPE_TRYOUT, typeTryout)
            fragment.arguments = args
            return fragment
        }
    }
}