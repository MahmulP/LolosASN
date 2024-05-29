package com.lolos.asn.ui.dialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.lolos.asn.R
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.FragmentStartDialogBinding
import com.lolos.asn.databinding.FragmentValidationDialogBinding
import com.lolos.asn.ui.activity.ExaminationActivity
import com.lolos.asn.ui.activity.ResultActivity

class ValidationDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentValidationDialogBinding
    private val examinationViewModel: ExaminationViewModel by activityViewModels()
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

        binding.btnDone.setOnClickListener {
            startActivity(Intent(requireContext(), ResultActivity::class.java))
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}