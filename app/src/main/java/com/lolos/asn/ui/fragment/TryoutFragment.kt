package com.lolos.asn.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentTryoutBinding
import com.lolos.asn.ui.activity.TryoutDetailActivity
import com.lolos.asn.ui.dialog.StartDialogFragment
import com.lolos.asn.ui.dialog.TryoutDialogFragment

class TryoutFragment : Fragment() {
    private var _binding: FragmentTryoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeTryout = arguments?.getString("typeTryout")

        if (typeTryout == "Premium") {
            binding.tvType.text = typeTryout
        } else {
            binding.tvType.text = typeTryout
            binding.tvType.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            binding.toolbar.title = "Tryout Gratis"
        }

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.btnDetail.setOnClickListener {
            startActivity(Intent(requireContext(), TryoutDetailActivity::class.java))
        }

        binding.btnMulai.setOnClickListener {
            val dialog = StartDialogFragment()
            dialog.show(parentFragmentManager, "TryoutFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTryoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}