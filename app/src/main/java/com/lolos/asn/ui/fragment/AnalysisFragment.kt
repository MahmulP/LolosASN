package com.lolos.asn.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentAnalysisBinding

class AnalysisFragment : Fragment() {
    private lateinit var binding: FragmentAnalysisBinding
    private var type: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            type = it.getString("type")
        }

        when (type) {
            "all" -> {
                binding.tvCategory.text = getString(R.string.analysis_all)
            }
            "twk" -> {
                binding.tvCategory.text = getString(R.string.tab_twk)
            }
            "tiu" -> {
                binding.tvCategory.text = getString(R.string.tab_tiu)
            }
            "tkp" -> {
                binding.tvCategory.text = getString(R.string.tab_tkp)
            }
        }
    }
}