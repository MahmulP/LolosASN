package com.lolos.asn.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.adapter.TryoutAdapter
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.TryoutResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.factory.TryoutViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.NotificationViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.FragmentHomeBinding
import com.lolos.asn.ui.activity.ArticleActivity
import com.lolos.asn.ui.activity.NotificationActivity
import com.lolos.asn.ui.activity.PurchaseActivity
import com.lolos.asn.ui.activity.ResultHistoryActivity
import com.lolos.asn.ui.dialog.TryoutDialogFragment
import com.lolos.asn.utils.MarginItemDecoration
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }
    private val tryoutViewModel: TryoutViewModel by viewModels {
        TryoutViewModelFactory(requireContext())
    }
    private val notificationViewModel by viewModels<NotificationViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.getAuthUser().observe(viewLifecycleOwner) {

            if (it.userId != null && it.token != null) {
                val userId = it.userId
                val token = "Bearer ${it.token}"
                authViewModel.getAUthUserData(userId, token)
                tryoutViewModel.getAllTryout(userId, token)
                notificationViewModel.getNotification(userId, token)
                tryoutViewModel.getNewestTryout(token)
            }
        }

        authViewModel.getUserData().observe(viewLifecycleOwner) {
            val fullName = it.name ?: ""
            val nameParts = fullName.split(" ")
            val displayName = if (nameParts.size >= 2) {
                "${nameParts[0]} ${nameParts[1]}"
            } else {
                fullName
            }

            binding.tvName.text = displayName
            binding.tvGreet.text = "Hai $displayName"

            val avatar = it.avatar

            Glide.with(this)
                .load(avatar)
                .error(R.drawable.avatar)
                .into(binding.ivUser)
        }

        tryoutViewModel.allTryout.observe(viewLifecycleOwner) { tryoutResponse ->
            tryoutResponse?.data?.let { dataList ->
                val isPremiumMember = dataList.any { it?.accessed == "1" }
                if (isPremiumMember) {
                    binding.tvMember.text = getString(R.string.premium_member)
                    binding.tvMember.visibility = View.VISIBLE
                } else {
                    binding.tvMember.text = getString(R.string.freemium_member)
                    binding.tvMember.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                    binding.tvMember.visibility = View.VISIBLE
                }
            }
        }

        notificationViewModel.notificationData.observe(viewLifecycleOwner) { notifications ->
            notifications?.data.let { list ->
                val hasUnclickedNotification = list?.any { it.isClicked == false }
                if (hasUnclickedNotification == true) {
                    binding.isActive.visibility = View.VISIBLE
                } else {
                    binding.isActive.visibility = View.GONE
                }
            }
        }



        tryoutViewModel.tryout.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
        }

        binding.ibTryout.setOnClickListener {
            val dialog = TryoutDialogFragment()
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        binding.ibMateri.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_learning)
        }

        binding.ibResult.setOnClickListener {
            startActivity(Intent(requireActivity(), ResultHistoryActivity::class.java))
        }

        binding.ibArtikel.setOnClickListener {
            startActivity(Intent(requireActivity(), ArticleActivity::class.java))
        }

        binding.ibCart.setOnClickListener {
            startActivity(Intent(requireActivity(), PurchaseActivity::class.java))
        }

        binding.ibNotification.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationActivity::class.java))
        }

        binding.tvAllTryout.setOnClickListener {
            val dialog = TryoutDialogFragment()
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        binding.tvAllArticle.setOnClickListener {
            startActivity(Intent(requireActivity(), ArticleActivity::class.java))
        }

        val greetingMessage = getGreetingMessage()
        binding.tvDateGreet.text = greetingMessage


    }

    private fun setupRecyclerView(tryoutResponse: TryoutResponse) {
        binding.rvTryout.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Set up the adapter
        val adapter = TryoutAdapter(requireContext())
        val density = requireContext().resources.displayMetrics.density
        val firstItemStartMargin = (32 * density).toInt()
        val restItemStartMargin = (16 * density).toInt()
        val lastItemEndMargin = (32 * density).toInt()

        val itemDecoration = MarginItemDecoration(firstItemStartMargin, restItemStartMargin, lastItemEndMargin)

        // Remove all item decorations to avoid duplicates
        val itemDecorationCount = binding.rvTryout.itemDecorationCount
        for (i in 0 until itemDecorationCount) {
            binding.rvTryout.removeItemDecorationAt(0)
        }

        binding.rvTryout.addItemDecoration(itemDecoration)
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> getString(R.string.greeting_morning)
            in 12..15 -> getString(R.string.greeting_afternoon)
            in 16..18 -> getString(R.string.greeting_evening)
            else -> getString(R.string.greeting_night)
        }
    }
}