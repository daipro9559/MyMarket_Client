package com.example.dainv.mymarket.ui.stand_followed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailViewModel
import com.example.dainv.mymarket.ui.stand.detail.StandDetailActivity
import com.example.dainv.mymarket.ui.stand.detail.StandDetailViewModel
import kotlinx.android.synthetic.main.activity_stand_followed.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import javax.inject.Inject

class StandFollowedActivity : BaseActivity(){
    @Inject
    lateinit var itemStandAdapter: StandFollowedAdapter
    private lateinit var standFollowedViewModel: StandFollowedViewModel
    private lateinit var stanDetailViewModel: StandDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stand_followed)
        setSupportActionBar(toolBar)
        enableHomeBack()
        standFollowedViewModel = ViewModelProviders.of(this,viewModelFactory)[StandFollowedViewModel::class.java]
        stanDetailViewModel = ViewModelProviders.of(this,viewModelFactory)[StandDetailViewModel::class.java]
        setTitle(R.string.stands_followed)
        initView()
        viewObserve()
        standFollowedViewModel.getStandsFollowed()
    }

    private fun viewObserve(){
        standFollowedViewModel.standsFollowed.observe(this, Observer {resourceWrapper->
            if (resourceWrapper!!.resourceState == ResourceState.LOADING){
                loadingLayout.visibility = View.VISIBLE
            }else{
                loadingLayout.visibility = View.GONE
            }
            if(resourceWrapper.resourceState == ResourceState.SUCCESS) {
                resourceWrapper!!.r?.let {
                    itemStandAdapter.swapItems(it.data)
                }
            }
        })

        itemStandAdapter.itemUnFollowedObserve.subscribe{
            stanDetailViewModel.unFollow(it)
        }
        stanDetailViewModel.unFollowResult.observe(this, Observer {resourceWrapper->

            if (resourceWrapper!!.resourceState == ResourceState.LOADING){
                loadingLayout.visibility = View.VISIBLE
            }else{
                loadingLayout.visibility = View.GONE
            }
            if(resourceWrapper.resourceState == ResourceState.SUCCESS) {
                standFollowedViewModel.getStandsFollowed()
            }
        })
        itemStandAdapter.itemClickObserve().observe(this, Observer {
            val intent = Intent(this, StandDetailActivity::class.java)
            intent.putExtra(StandDetailActivity.STAND_KEY,it)
            intent.putExtra(StandDetailActivity.IS_MY_STAND,false)
            startActivityWithAnimation(intent)
            finish()
        })
    }
    private fun initView(){
        recycleView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        recycleView.adapter = itemStandAdapter
    }
}