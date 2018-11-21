package com.example.dainv.mymarket.ui.stand.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.model.Stand
import kotlinx.android.synthetic.main.fragment_stand_infor.*

class StandInformationFragment :BaseFragment(){
    companion object {
        fun newInstance(): StandInformationFragment {
            val standInformationFragment = StandInformationFragment()
            return standInformationFragment
        }
    }
    private var stand: Stand? = null
    private var isMystand: Boolean = false
    lateinit var standDetailViewModel: StandDetailViewModel
    override fun getLayoutID() = R.layout.fragment_stand_infor

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        standDetailViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[StandDetailViewModel::class.java]
        getDataFromItem()
        initView()
        viewObserve()
        standDetailViewModel.getUserProfile(stand!!.userID!!)
    }

    private fun viewObserve() {
        standDetailViewModel.profileResult.observe(this, Observer {
            it?.r?.let {user->
                txtPhone.text = user.phone
            }
        })
        standDetailViewModel.followResult.observe(this, Observer {
            btnFollow.isEnabled = it!!.resourceState != ResourceState.LOADING
            it?.r?.let {success->
                if (success){
                    stand!!.isFollowed = true

                    btnFollow.text = getString(R.string.un_follow)
                    val snackbar = Snackbar.make(nestedScrollView,R.string.follow_completed,Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }else{
                    val snackbar = Snackbar.make(nestedScrollView,R.string.follow_un_completed,Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        })
        standDetailViewModel.unFollowResult.observe(this, Observer {
            btnFollow.isEnabled = it!!.resourceState != ResourceState.LOADING
            it?.r?.let {success->
                if (success){
                    stand!!.isFollowed = false
                    btnFollow.text = getString(R.string.follow)
                    val snackbar = Snackbar.make(nestedScrollView,R.string.un_follow_completed,Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }else{
                    val snackbar = Snackbar.make(nestedScrollView,R.string.un_follow_un_completed,Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        })
    }

    private fun initView(){
        if (isMystand){
            btnFollow.visibility = View.GONE
        }else{
            btnFollow.text = if(stand!!.isFollowed) getString(R.string.un_follow) else getString(R.string.follow)
            btnFollow.setOnClickListener {
                if(stand!!.isFollowed) standDetailViewModel.unFollow(stand!!.standID) else standDetailViewModel.follow(stand!!.standID)
            }
        }
        stand?.let {
            txtIntroduction.text = it.description
            txtLocation.text = it.Address!!.address
        }
    }
    private fun getDataFromItem() {
        stand = activity!!.intent.getParcelableExtra(StandDetailActivity.STAND_KEY)
        isMystand = activity!!.intent.getBooleanExtra(StandDetailActivity.IS_MY_STAND, false)
    }
}
