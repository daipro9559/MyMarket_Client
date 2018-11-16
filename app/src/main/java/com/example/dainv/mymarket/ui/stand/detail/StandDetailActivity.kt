package com.example.dainv.mymarket.ui.stand.detail

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.transition.Transition
import android.view.View
import android.view.Window
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.glide.GlideApp
import com.example.dainv.mymarket.model.Stand
import kotlinx.android.synthetic.main.activity_stand.*

class StandDetailActivity :BaseActivity() {
    companion object {
        val IS_MY_STAND = "is_my_stand"
        val STAND_KEY = "stand key"
    }
    private lateinit var stand:Stand
    private  var isMystand:Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stand)
        setSupportActionBar(toolBar)
        enableHomeBack()
        // get stand
        getDataFromItem()
        initView()
    }
    private fun initView(){
        collapsingLayout.title = stand.name
//        collapsingLayout.setExpandedTitleColor(resources.getColor(R.color.colorTransparent))
        GlideApp.with(applicationContext)
                .load(Constant.BASE_URL+stand.image)
                .into(imageCollapse)
        if (isMystand){

        }
    }
    private fun getDataFromItem(){
        stand = intent.getParcelableExtra(STAND_KEY)
        isMystand = intent.getBooleanExtra(IS_MY_STAND,false)
    }
}