package com.lolos.asn.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lolos.asn.R
import com.lolos.asn.data.data.UserData
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.ExaminationResponse
import com.lolos.asn.data.response.TryoutRequest
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.FragmentValidationDialogBinding
import com.lolos.asn.ui.activity.ResultActivity

class ValidationDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentValidationDialogBinding
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private val examinationViewModel: ExaminationViewModel by activityViewModels()

    private var currentRequest: TryoutRequest? = null
    private var currentTryoutData: ExaminationResponse? = null
    private var currentUserData: UserData? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValidationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        examinationViewModel.examTryout.observe(viewLifecycleOwner) { questions ->
            val quizTotal = questions.size
            examinationViewModel.selectedAnswers.observe(viewLifecycleOwner) { answeredQuestions ->
                val answeredQuestion = answeredQuestions.size
                val message = getString(R.string.end_the_test, answeredQuestion, quizTotal)
                binding.tvText.text = message
            }
        }

        examinationViewModel.tryoutRequest.observe(viewLifecycleOwner) { request ->
            currentRequest = request
        }

        examinationViewModel.tryoutData.observe(viewLifecycleOwner) { tryoutData ->
            currentTryoutData = tryoutData
        }

        authViewModel.getAuthUser().observe(viewLifecycleOwner) { userData ->
            currentUserData = userData
        }

        binding.btnDone.setOnClickListener {
            handleFinishTryout()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun handleFinishTryout() {
        val request = currentRequest
        val tryoutData = currentTryoutData
        val userData = currentUserData

        if (request != null && tryoutData != null && userData != null) {
            val tryoutId = tryoutData.data.tryoutId
            val token = "Bearer ${userData.token}"
            examinationViewModel.finishTryout(userId = userData.userId, tryoutId = tryoutId, tryoutRequest = request, token)

            examinationViewModel.isFinish.observe(viewLifecycleOwner) { result ->
                if (result) {
                    val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                        putExtra("tryout_id", tryoutId)
                    }
                    startActivity(intent)
                    dismiss()
                } else {
                    showToast("Gagal menyelesaikan")
                }
            }
        } else {
            showToast("Required data is missing")
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}