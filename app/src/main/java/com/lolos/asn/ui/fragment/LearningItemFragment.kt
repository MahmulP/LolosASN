package com.lolos.asn.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentLearningItemBinding
import com.lolos.asn.ui.activity.LearningDetailActivity

class LearningItemFragment : Fragment() {

    private var _binding: FragmentLearningItemBinding? = null
    private val binding get() = _binding!!
    private var type: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            type = it.getString("type")
        }

        when (type) {
            "twk" -> {
                binding.tvCategory.text = getString(R.string.twk)
            }
            "tiu" -> {
                binding.tvCategory.text = getString(R.string.tiu)
            }
            "tkp" -> {
                binding.tvCategory.text = getString(R.string.tkp)
            }
        }

        val bookButton = binding.tvBook

        bookButton.setOnClickListener {
            startActivity(Intent(requireContext(), LearningDetailActivity::class.java))
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLearningItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
    }
}