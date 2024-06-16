package com.lolos.asn.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.lolos.asn.data.response.DrillingRequest
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.DrillingStartViewModel
import com.lolos.asn.databinding.FragmentValidationDrillingFinishBinding

class ValidationDrillingFinishFragment : DialogFragment() {
    private lateinit var binding: FragmentValidationDrillingFinishBinding

    private var latsolId: String? = null
    private var userId: String? = null
    private lateinit var currentRequest: DrillingRequest
    private var authToken: String = "token"

    private val drillingStartViewModel: DrillingStartViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latsolId = it.getString(ARG_LATSOL_ID)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentValidationDrillingFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.getAuthUser().observe(viewLifecycleOwner) {
            userId = it.userId
            authToken = "Bearer ${it.token}"
        }

        drillingStartViewModel.drillingRequest.observe(this) {
            currentRequest = it
        }

        binding.btnDone.setOnClickListener {
            drillingStartViewModel.finishDrilling(userId = userId, latsolId = latsolId, drillingRequest = currentRequest, token = authToken)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
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
        private const val ARG_LATSOL_ID = "latsolId"

        fun newInstance(latsolId: String?): ValidationDrillingFinishFragment {
            val fragment = ValidationDrillingFinishFragment()
            val args = Bundle()
            args.putString(ARG_LATSOL_ID, latsolId)
            fragment.arguments = args
            return fragment
        }
    }

}