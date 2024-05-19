package com.lolos.asn.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentHomeBinding
import com.lolos.asn.databinding.FragmentLearningBinding
import com.lolos.asn.databinding.FragmentTwkBinding
import com.lolos.asn.ui.activity.LearningDetailActivity
import com.lolos.asn.ui.dialog.TryoutDialogFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibTryout.setOnClickListener {
            val dialog = TryoutDialogFragment()
            dialog.show(parentFragmentManager, "CustomDialog")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}