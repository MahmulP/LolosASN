package com.lolos.asn.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lolos.asn.ui.fragment.AnalysisFragment

class AnalysisSectionsPageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = AnalysisFragment()
                fragment.arguments = bundleOf("type" to "all")
            }
            1 -> {
                fragment = AnalysisFragment()
                fragment.arguments = bundleOf("type" to "twk")
            }
            2 -> {
                fragment = AnalysisFragment()
                fragment.arguments = bundleOf("type" to "tiu")
            }
            3 -> {
                fragment = AnalysisFragment()
                fragment.arguments = bundleOf("type" to "tkp")
            }
        }
        return fragment as Fragment
    }
}