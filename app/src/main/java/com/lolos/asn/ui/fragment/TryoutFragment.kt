package com.lolos.asn.ui.fragment

import android.content.Intent
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
import com.lolos.asn.adapter.TryoutFragmentAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.TryoutResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.FragmentTryoutBinding
import com.lolos.asn.ui.activity.ResultHistoryActivity
import com.lolos.asn.ui.dialog.RedeemTokenDialog

class TryoutFragment : Fragment() {
    private var _binding: FragmentTryoutBinding? = null
    private val binding get() = _binding!!

    private val tryoutViewModel: TryoutViewModel by activityViewModels()

    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var userId: String? = null
    private var token: String = "token"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tryoutViewModel.isEmpty.observe(viewLifecycleOwner) {
            showEmpty(it)
        }

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.cvTryoutHistory.setOnClickListener {
            val intent = Intent(requireActivity(), ResultHistoryActivity::class.java)
            intent.putExtra("transactionFrom", "TryoutFragment")
            startActivity(intent)
        }

        binding.btnRedeem.setOnClickListener {
            val typeTryout = arguments?.getString("typeTryout") ?: ""
            val dialog = RedeemTokenDialog.newInstance(typeTryout)
            dialog.show(parentFragmentManager, "RedeemTokenDialog")
        }

        handleTryout()
    }

    private fun handleTryout() {
        val typeTryout = arguments?.getString("typeTryout")

        if (typeTryout == "Premium") {
            authViewModel.getAuthUser().observe(viewLifecycleOwner) {userData ->
                if (userData.userId != null) {
                    userId = userData.userId
                    token = "Bearer ${userData.token}"
                    tryoutViewModel.getPaidTryout(userId, token)
                }
            }

            tryoutViewModel.paidTryout.observe(viewLifecycleOwner) {
                setupRecyclerView(it)
            }

            binding.tvType.text = typeTryout
        } else {
            authViewModel.getAuthUser().observe(viewLifecycleOwner) {userData ->
                if (userData.userId != null) {
                    userId = userData.userId
                    token = "Bearer ${userData.token}"
                    tryoutViewModel.getFreeTryout(userId, token)
                }
            }
            tryoutViewModel.freeTryout.observe(viewLifecycleOwner) {
                setupRecyclerView(it)
            }

            binding.tvType.text = typeTryout
            binding.tvType.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            binding.toolbar.title = "Tryout Gratis"
        }

        tryoutViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showEmpty(status: Boolean) {
        if (status) {
            binding.ivEmpty.visibility = View.VISIBLE
        } else {
            binding.ivEmpty.visibility = View.GONE
        }
    }

    private fun setupRecyclerView(tryoutResponse: TryoutResponse?) {
        binding.rvTryout.layoutManager = LinearLayoutManager(requireActivity())

        // Set up the adapter
        val adapter = TryoutFragmentAdapter(requireContext())
        binding.rvTryout.adapter = adapter

        // Submit the list to the adapter
        tryoutResponse?.data.let {
            adapter.submitList(it)
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