package com.lolos.asn.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lolos.asn.ui.fragment.LearningItemFragment

class LearningSectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = LearningItemFragment()
                fragment.arguments = bundleOf("type" to "twk")
            }
            1 -> {
                fragment = LearningItemFragment()
                fragment.arguments = bundleOf("type" to "tiu")
            }
            2 -> {
                fragment = LearningItemFragment()
                fragment.arguments = bundleOf("type" to "tkp")
            }
        }
        return fragment as Fragment
    }

}