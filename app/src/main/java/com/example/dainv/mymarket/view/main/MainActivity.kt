package com.example.dainv.mymarket.view.main

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import com.example.dainv.mymarket.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutParams = bottomNavigation.layoutParams as CoordinatorLayout.LayoutParams
//        layoutParams.behavior = BottomNavigationBehavior()
    }
}