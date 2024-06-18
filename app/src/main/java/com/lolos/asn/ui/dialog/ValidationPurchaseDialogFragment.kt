package com.lolos.asn.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lolos.asn.R
import com.lolos.asn.adapter.DescriptionAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.FragmentValidationPurchaseDialogBinding
import com.lolos.asn.ui.activity.DetailPurchaseActivity
import java.text.NumberFormat
import java.util.Locale

class ValidationPurchaseDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentValidationPurchaseDialogBinding
    val tryoutViewModel: TryoutViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValidationPurchaseDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bundleId = arguments?.getString("bundle_id")
        authViewModel.getAuthUser().observe(viewLifecycleOwner) {
            if (it?.token != null) {
                val userId = it.userId
                val token = "Bearer ${it.token}"
                tryoutViewModel.getBundleTryoutDetail(userId = userId, bundleId = bundleId, token = token)
            }
        }

        tryoutViewModel.bundleTryoutDetail.observe(viewLifecycleOwner) { bundleDetail ->
            if (bundleDetail != null) {
                val price = bundleDetail.data.price
                val basePrice = bundleDetail.data.basePrice
                val priceFormat = price.toInt()
                val basePriceFormat = basePrice
                val indonesianLocale = Locale("in", "ID")
                val formattedPrice = NumberFormat.getNumberInstance(indonesianLocale).format(priceFormat)
                val formattedBasePrice = NumberFormat.getNumberInstance(indonesianLocale).format(basePriceFormat)

                binding.tvTitle.text = bundleDetail.data.tryoutBundleName
                binding.tvDescription.text = bundleDetail.data.description
                binding.tvPrice.text = getString(R.string.price, formattedPrice)
                if (price.toInt() == basePrice) {
                    binding.tvNormalPrice.visibility = View.GONE
                } else {
                    binding.tvNormalPrice.text = getString(R.string.price, formattedBasePrice)
                }

                binding.btnBuy.setOnClickListener {
                    val intent = Intent(requireContext(), DetailPurchaseActivity::class.java)
                    intent.putExtra("bundle_id", bundleDetail.data.tryoutBundleId)
                    startActivity(intent)
                    dismiss()
                }

                bundleDetail.data.descList.let { text ->
                    setupRecycleView(text)
                }
            }
        }
    }

    private fun setupRecycleView(text: List<String?>?) {
        binding.rvDescription.layoutManager = LinearLayoutManager(requireContext())

        val adapter = DescriptionAdapter(requireContext())
        binding.rvDescription.adapter = adapter

        // Submit the list to the adapter
        text.let {
            adapter.submitList(it)
        }
    }

    companion object {
        fun newInstance(bundleId: String?): ValidationPurchaseDialogFragment {
            val fragment = ValidationPurchaseDialogFragment()
            val args = Bundle()
            args.putString("bundle_id", bundleId)
            fragment.arguments = args
            return fragment
        }
    }
}