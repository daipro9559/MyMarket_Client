package com.example.dainv.mymarket.ui.main.profile

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment

class ProfileFragment :BaseFragment() {

    companion object {
        val TAG = "profile fragment"
        fun newInstance():ProfileFragment{
            val profileFragment = ProfileFragment()
            return profileFragment
        }
    }
    override fun getLayoutID() = R.layout.fragment_profile
}