package com.example.dainv.mymarket.ui.main.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.databinding.FragmentCategoryBinding
import com.example.dainv.mymarket.databinding.FragmentProfileBinding
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.items.ListItemActivity
import com.example.dainv.mymarket.ui.marked.ItemsMarkedActivity
import com.example.dainv.mymarket.ui.my.items.MyItemsActivity
import com.example.dainv.mymarket.ui.my.stands.MyStandsActivity
import com.example.dainv.mymarket.ui.notification.SettingNotificationActivity
import com.example.dainv.mymarket.ui.transaction.TransactionActivity
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
            loadingLayout.visibility = if (it?.resourceState == ResourceState.LOADING)  View.VISIBLE else View.GONE
            Timber.e("get profile state ${it!!.resourceState.toString()}")
            viewDataBinding!!.profileSource = it
            if(it!!.resourceState == ResourceState.SUCCESS){
                txtName.text = it.r!!.name
                txtPhone.text = it.r!!.phone
            }
        })
        profileViewModel.logoutResult.observe(this, Observer {
            loadingLayout.visibility = if (it?.resourceState == ResourceState.LOADING)  View.VISIBLE else View.GONE
            it?.r?.let {success->
                if(success){
                    logout()
                }
            }
        })
    }



    private fun initView(){
        icLogout.setOnClickListener{
            profileViewModel.logout()
        }
        txtMyStand.setOnClickListener {
            startActivity(Intent(activity,MyStandsActivity::class.java))
        }
        txtItemUploaded.setOnClickListener {
            val intent = Intent(activity,MyItemsActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(ListItemActivity.IS_MY_ITEM_KEY,true)
            intent.putExtra("bundle",bundle)
            startActivityWithAnimation(intent)
        }
        txtItemMarked.setOnClickListener {
            startActivityWithAnimation(Intent(activity,ItemsMarkedActivity::class.java))
        }
        txtNotification.setOnClickListener {
            startActivityWithAnimation(Intent(activity,SettingNotificationActivity::class.java))
        }
        txtChangePass.setOnClickListener {
            ChangePassDialog.newInstance().show(fragmentManager,ChangePassDialog.javaClass.name)
        }
        txtTransaction.setOnClickListener {
            val intent = Intent(activity,TransactionActivity::class.java)
            startActivityWithAnimation(intent)
        }
    }
}