package com.lolos.asn.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentHomeBinding
import com.lolos.asn.ui.activity.ArticleActivity
import com.lolos.asn.ui.activity.ResultActivity
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

        binding.ibMateri.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_learning)
        }

        binding.ibResult.setOnClickListener {
            startActivity(Intent(requireActivity(), ResultActivity::class.java))
        }

        binding.ibArtikel.setOnClickListener {
            startActivity(Intent(requireActivity(), ArticleActivity::class.java))
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}