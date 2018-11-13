package com.example.dainv.mymarket.ui.main.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.databinding.FragmentCategoryBinding
import com.example.dainv.mymarket.databinding.FragmentProfileBinding
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.my.stands.MyStandsActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber

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
     var viewDataBinding: FragmentProfileBinding? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding = DataBindingUtil.bind(view!!)
        profileViewModel = ViewModelProviders.of(this,viewModelFactory)[ProfileViewModel::class.java]
        initView()
        profileViewModel.getProfile()
        profileViewModel.profileLiveData.observe(this, Observer {
            Timber.e("get profile state ${it!!.resourceState.toString()}")
            viewDataBinding!!.profileSource = it
            if(it!!.resourceState == ResourceState.SUCCESS){
                txtName.text = it.r!!.name
                txtPhone.text = it.r!!.phone
            }
        })
    }



    private fun initView(){
        icLogout.setOnClickListener{
            logout()
        }
        txtStand.setOnClickListener {
            startActivity(Intent(activity,MyStandsActivity::class.java))
        }

    }
}