package com.lolos.asn.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lolos.asn.ui.fragment.TIUFragment
import com.lolos.asn.ui.fragment.TKPFragment
import com.lolos.asn.ui.fragment.TWKFragment

class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = TWKFragment()
            1 -> fragment = TIUFragment()
            2 -> fragment = TKPFragment()
        }
        return fragment as Fragment
    }

}