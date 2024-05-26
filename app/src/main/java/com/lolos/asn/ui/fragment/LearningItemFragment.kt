package com.lolos.asn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.CourseAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.CourseResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.CourseViewModel
import com.lolos.asn.databinding.FragmentLearningItemBinding

class LearningItemFragment : Fragment() {

    private var _binding: FragmentLearningItemBinding? = null
    private val binding get() = _binding!!
    private var type: String? = null

    private val courseViewModel by viewModels<CourseViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            type = it.getString("type")
        }

        when (type) {
            "twk" -> {
                binding.tvCategory.text = getString(R.string.twk)
                authViewModel.getAuthUser().observe(viewLifecycleOwner) {
                    val userId = it.userId
                    courseViewModel.getCourses("$userId", "1")
                }

                courseViewModel.courses.observe(viewLifecycleOwner) {
                    setupRecyclerView(it)
                }

                courseViewModel.isLoading.observe(viewLifecycleOwner) {
                    showLoading(it)
                }
            }
            "tiu" -> {
                binding.tvCategory.text = getString(R.string.tiu)
                authViewModel.getAuthUser().observe(viewLifecycleOwner) {
                    val userId = it.userId
                    courseViewModel.getCourses("$userId", "2")
                }

                courseViewModel.courses.observe(viewLifecycleOwner) {
                    setupRecyclerView(it)
                }

                courseViewModel.isLoading.observe(viewLifecycleOwner) {
                    showLoading(it)
                }
            }
            "tkp" -> {
                binding.tvCategory.text = getString(R.string.tkp)
                authViewModel.getAuthUser().observe(viewLifecycleOwner) {
                    val userId = it.userId
                    courseViewModel.getCourses("$userId", "3")
                }

                courseViewModel.courses.observe(viewLifecycleOwner) {
                    setupRecyclerView(it)
                }

                courseViewModel.isLoading.observe(viewLifecycleOwner) {
                    showLoading(it)
                }
            }
        }
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
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

    private fun setupRecyclerView(courseResponse: CourseResponse) {
        binding.rvCourses.layoutManager = LinearLayoutManager(requireContext())

        // Set up the adapter
        val adapter = CourseAdapter(requireContext())
        binding.rvCourses.adapter = adapter

        // Submit the list to the adapter
        courseResponse.data.let {
            adapter.submitList(it)
        }
    }

    companion object
}