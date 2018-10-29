package com.example.dainv.mymarket.ui.main.profile

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment :BaseFragment() {

    lateinit var profileViewModel: ProfileViewModel
    companion object {
        val TAG = "profile fragment"
        fun newInstance():ProfileFragment{
            val profileFragment = ProfileFragment()
            return profileFragment
        }
    }
    override fun getLayoutID() = R.layout.fragment_profile

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel = ViewModelProviders.of(this,viewModelFactory)[ProfileViewModel::class.java]
        initView()
    }

    private fun initView(){
        icLogout.setOnClickListener{
            logout()
        }
    }
}