package com.lolos.asn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.DrillingAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.DrillingResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.DrillingViewModel
import com.lolos.asn.databinding.FragmentDrillingBinding

class DrillingFragment : Fragment() {
    private lateinit var binding: FragmentDrillingBinding

    private val drillingViewModel by viewModels<DrillingViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDrillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationIconTint(ContextCompat.getColor(requireContext(), R.color.white))
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        authViewModel.getAuthUser().observe(viewLifecycleOwner) { userData ->
            val token = "Bearer ${userData.token}"
            drillingViewModel.getAllDrilling(token)
        }

        drillingViewModel.drilling.observe(viewLifecycleOwner) { drilling ->
            if (drilling != null) {
                setupRecycleView(drilling)
            }
        }
    }

    private fun setupRecycleView(drillingResponse: DrillingResponse) {
        binding.rvLatsol.layoutManager = LinearLayoutManager(requireContext())

        val adapter = DrillingAdapter(requireContext())
        binding.rvLatsol.adapter = adapter

        drillingResponse.data.let {
            adapter.submitList(it)
        }
    }
}