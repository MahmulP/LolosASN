package com.lolos.asn.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.DialogNumberAdapter
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.FragmentNumberDialogBinding

class NumberDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentNumberDialogBinding
    private val examinationViewModel: ExaminationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        binding.btnDone.setOnClickListener {
            val dialog = ValidationDialogFragment()
            dialog.show(parentFragmentManager, "ValidationDialogFragment")
        }

        examinationViewModel.examTryout.observe(viewLifecycleOwner) { questions ->
            setupRecycleView(questions)
        }

        examinationViewModel.remainingTime.observe(viewLifecycleOwner) { time ->
            binding.tvTime.text = time
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialogTheme)
    }

    private fun setupRecycleView(tryoutContentItems: List<TryoutContentItem>) {
        binding.rvNumber.layoutManager = GridLayoutManager(requireContext(), 5)
        val adapter = DialogNumberAdapter(examinationViewModel, this)
        binding.rvNumber.adapter = adapter

        adapter.submitList(tryoutContentItems)
    }
}

