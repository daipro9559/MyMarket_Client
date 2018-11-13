package com.example.dainv.mymarket.ui.my.stands

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.create.stand.CreateStandActivity
import com.example.dainv.mymarket.util.SharePreferencHelper
import kotlinx.android.synthetic.main.activity_my_stands.*
import kotlinx.android.synthetic.main.app_bar_layout.view.*
import javax.inject.Inject

class MyStandsActivity :BaseActivity() {
    @Inject
    lateinit var sharePreferencHelper: SharePreferencHelper
    private lateinit var myStandsViewModel: MyStandsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_stands)
        setSupportActionBar(appBarLayout.toolBar)
        setTitle(R.string.manager_stand)
        enableHomeBack()
        myStandsViewModel = ViewModelProviders.of(this,viewModelFactory)[MyStandsViewModel::class.java]
        initView()
        viewObserve()
    }

    private fun initView(){
        if (sharePreferencHelper.getInt(Constant.USER_TYPE) == 0){
            cardRequest.visibility = View.VISIBLE
            floatBtnAdd.hide()
        }else{
            floatBtnAdd.show()
            myStandsViewModel.getMyStands()
        }
        btnUpdateToSeller.setOnClickListener {
            myStandsViewModel.updateToSeller()
        }
        floatBtnAdd.setOnClickListener {
            startActivity(Intent(this,CreateStandActivity::class.java))
        }
    }

    private fun viewObserve(){
        myStandsViewModel.updateToSellerResult.observe(this, Observer {
            it?.r?.let {result->
                if(result){
                    cardRequest.visibility = View.GONE
                    floatBtnAdd.show()
                }
            }
        })

        myStandsViewModel.listMyStand.observe(this, Observer {

            if (it!!.resourceState == ResourceState.LOADING){
                loadingLayout.visibility = View.VISIBLE
            }else{
                loadingLayout.visibility = View.GONE
            }
        })
    }
}