package com.lolos.asn.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lolos.asn.R
import com.lolos.asn.data.viewmodel.model.AnalysisViewModel
import com.lolos.asn.databinding.FragmentAnalysisBinding

class AnalysisFragment : Fragment() {
    private lateinit var binding: FragmentAnalysisBinding
    private var type: String? = null

    private val analysisViewModel: AnalysisViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            type = it.getString("type")
        }

        analysisViewModel.analysisAi.observe(viewLifecycleOwner) {
            when (type) {
                "all" -> {
                    binding.tvCategory.text = getString(R.string.analysis_all)
                    binding.tvDescription.text = "${it.feedback?.twk}${it.feedback?.tiu}${it.feedback?.tkp}"
                }
                "twk" -> {
                    binding.tvCategory.text = getString(R.string.twk)
                    binding.tvDescription.text = it.feedback?.twk
                }
                "tiu" -> {
                    binding.tvCategory.text = getString(R.string.tiu)
                    binding.tvDescription.text = it.feedback?.tiu
                }
                "tkp" -> {
                    binding.tvCategory.text = getString(R.string.tkp)
                    binding.tvDescription.text = it.feedback?.tkp
                }
            }
        }

        analysisViewModel.isEmpty.observe(viewLifecycleOwner) {
            showEmpty(it)
        }
    }

    private fun showEmpty(it: Boolean) {
        binding.ivEmpty.visibility = if (it) View.VISIBLE else View.GONE
    }
}