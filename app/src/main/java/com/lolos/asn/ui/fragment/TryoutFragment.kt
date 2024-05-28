package com.lolos.asn.ui.fragment

import android.content.Intent
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
import com.lolos.asn.adapter.TryoutAdapter
import com.lolos.asn.adapter.TryoutFragmentAdapter
import com.lolos.asn.data.response.TryoutResponse
import com.lolos.asn.data.viewmodel.model.CourseViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.FragmentTryoutBinding
import com.lolos.asn.ui.activity.TryoutDetailActivity
import com.lolos.asn.ui.dialog.StartDialogFragment
import com.lolos.asn.ui.dialog.TryoutDialogFragment
import com.lolos.asn.utils.MarginItemDecoration

class TryoutFragment : Fragment() {
    private var _binding: FragmentTryoutBinding? = null
    private val binding get() = _binding!!

    private val tryoutViewModel by viewModels<TryoutViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeTryout = arguments?.getString("typeTryout")

        if (typeTryout == "Premium") {
            tryoutViewModel.getPaidTryout()
            tryoutViewModel.paidTryout.observe(viewLifecycleOwner) {
                setupRecyclerView(it)
            }

            binding.tvType.text = typeTryout
        } else {
            tryoutViewModel.getFreeTryout()
            tryoutViewModel.freeTryout.observe(viewLifecycleOwner) {
                setupRecyclerView(it)
            }

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
    }

    private fun setupRecyclerView(tryoutResponse: TryoutResponse) {
        binding.rvTryout.layoutManager = LinearLayoutManager(requireContext())

        // Set up the adapter
        val adapter = TryoutFragmentAdapter(requireContext())
        binding.rvTryout.adapter = adapter

        // Submit the list to the adapter
        tryoutResponse.data.let {
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