package com.lolos.asn.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.lolos.asn.R
import com.lolos.asn.data.viewmodel.model.DrillingViewModel
import com.lolos.asn.databinding.FragmentValidationDrillingStartBinding
import com.lolos.asn.ui.activity.DrillingExamActivity

class ValidationDrillingStartFragment : DialogFragment() {
    private lateinit var binding: FragmentValidationDrillingStartBinding
    private val drillingViewModel: DrillingViewModel by activityViewModels()
    private var latsolId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentValidationDrillingStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drillingViewModel.drillingDetail.observe(viewLifecycleOwner) { detail ->
            binding.tvTotalSoal.text = getString(R.string.total_soal, detail?.data?.jumlahSoal)
            binding.tvTotalTime.text = getString(R.string.total_time, detail?.data?.waktu?.div(60))
            latsolId = detail?.data?.latsolId
        }

        binding.btnStart.setOnClickListener {
            val intent = Intent(requireContext(), DrillingExamActivity::class.java)
            intent.putExtra("latsol_id", latsolId)
            startActivity(intent)
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
}