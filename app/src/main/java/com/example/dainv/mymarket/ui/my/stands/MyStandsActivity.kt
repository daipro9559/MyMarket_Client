package com.example.dainv.mymarket.ui.my.stands

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.common.ItemStandAdapter
import com.example.dainv.mymarket.ui.create.stand.CreateStandActivity
import com.example.dainv.mymarket.ui.stand.detail.StandDetailActivity
import com.example.dainv.mymarket.util.SharePreferencHelper
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_my_stands.*
import kotlinx.android.synthetic.main.app_bar_layout.view.*
import javax.inject.Inject

class MyStandsActivity : BaseActivity() {
    companion object {
        val CREATE_STAND_CODE = 121
    }
    @Inject
    lateinit var sharePreferencHelper: SharePreferencHelper
    @Inject
    lateinit var itemStanddapter : Lazy<ItemStandAdapter>

    private lateinit var myStandsViewModel: MyStandsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_stands)
        setSupportActionBar(appBarLayout.toolBar)
        setTitle(R.string.my_stand)
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
            startActivityForResult(Intent(this,CreateStandActivity::class.java), CREATE_STAND_CODE)
        }
        recycleView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycleView.addItemDecoration(DividerItemDecoration(applicationContext,LinearLayoutManager.VERTICAL))
        recycleView.adapter = itemStanddapter.get()
    }

    private fun viewObserve(){
        itemStanddapter.get().itemClickObserve().observe(this, Observer {
            val intent = Intent(this, StandDetailActivity::class.java)
            intent.putExtra(StandDetailActivity.STAND_KEY,it)
            intent.putExtra(StandDetailActivity.IS_MY_STAND,true)
            startActivity(intent)
        })
        myStandsViewModel.updateToSellerResult.observe(this, Observer {
            it?.r?.let {result->
                if(result){
                    cardRequest.visibility = View.GONE
                    floatBtnAdd.show()
                }
            }
        })

        myStandsViewModel.listMyStand.observe(this, Observer { resourceWrapper ->

            if (resourceWrapper!!.resourceState == ResourceState.LOADING){
                loadingLayout.visibility = View.VISIBLE
            }else{
                loadingLayout.visibility = View.GONE
            }
            resourceWrapper!!.r?.let {
                itemStanddapter.get().swapItems(it.data)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== CREATE_STAND_CODE){
            if (resultCode == Activity.RESULT_OK){
                myStandsViewModel.getMyStands()
            }
        }
    }
}