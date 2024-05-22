package com.lolos.asn.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentTermPrivacyBinding

class TermPrivacyFragment : Fragment() {
    private lateinit var binding: FragmentTermPrivacyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTermPrivacyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        val type = arguments?.getString("type")

        if (type == "Privacy") {
            toolbar.title = getString(R.string.title_privacy_term)
            binding.tvTitle.text = getString(R.string.title_privacy_term)
            binding.tvDescription.text = getString(R.string.lorem_long)
        } else {
            toolbar.title = getString(R.string.title_term_and_condition)
            binding.tvTitle.text = getString(R.string.title_term_and_condition)
            binding.tvDescription.text = getString(R.string.lorem_long)
        }
    }
}