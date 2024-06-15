package com.lolos.asn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.DrillingHistoryAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.DrillingHistoryResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.DrillingViewModel
import com.lolos.asn.databinding.FragmentDrillingDetailBinding
import com.lolos.asn.ui.dialog.ValidationDrillingStartFragment

class DrillingDetailFragment : Fragment() {
    private lateinit var binding: FragmentDrillingDetailBinding

    private val drillingViewModel: DrillingViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDrillingDetailBinding.inflate(inflater, container, false)
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

        val latsolId = arguments?.getString("latsol_id")

        authViewModel.getAuthUser().observe(viewLifecycleOwner){ userData ->
            val token = "Bearer ${userData.token}"
            drillingViewModel.getDrillingDetail(latsolId = latsolId, token = token)
            drillingViewModel.getHistoryDrilling(latsolId = latsolId, userId = userData.userId, token = token)
        }

        drillingViewModel.drillingDetail.observe(viewLifecycleOwner){ detail ->
            binding.toolbar.title = detail?.data?.category?.categoryName
            binding.tvTitle.text = detail?.data?.category?.categoryAlias
            binding.tvDescription.text = detail?.data?.latDesc
            binding.tvDrillingTitle.text = detail?.data?.category?.categoryAlias
            binding.tvDrillingTitle.text = getString(R.string.drilling_category, detail?.data?.category?.categoryAlias)
            binding.tvTotalSoal.text = getString(R.string.total_soal, detail?.data?.jumlahSoal)
            binding.tvTotalTime.text = getString(R.string.total_time, (detail?.data?.waktu?.div(60)))
        }

        drillingViewModel.drillingHistory.observe(viewLifecycleOwner){ history ->
            if (history != null) {
                setupRecycleView(history)
            }
        }

        drillingViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        drillingViewModel.isEmpty.observe(viewLifecycleOwner) {
            showEmpty(it)
        }

        binding.btnStart.setOnClickListener {
            val dialog = ValidationDrillingStartFragment()
            dialog.show(parentFragmentManager, "ValidationDrilling")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmpty(isEmpty: Boolean) {
        binding.ivEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun setupRecycleView(history: DrillingHistoryResponse) {
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        val adapter = DrillingHistoryAdapter(requireContext())
        binding.rvHistory.adapter = adapter

        history.data.let {
            adapter.submitList(it)
        }
    }
}