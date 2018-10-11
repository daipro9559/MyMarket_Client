package com.example.dainv.mymarket.view.main

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.view.main.category.CategoryFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home ->{
                    replaceFragment(CategoryFragment.newInstance(),"")
                }
            }
            true
        }
        replaceFragment(CategoryFragment.newInstance(),"")
    }
    private fun replaceFragment(fragment: Fragment,tag:String){
        supportFragmentManager.beginTransaction()
                .replace(R.id.viewContainer,fragment,tag)
                .disallowAddToBackStack()
                .commit()
    }
}