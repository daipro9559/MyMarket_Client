package com.example.dainv.mymarket.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import com.example.dainv.mymarket.ui.main.category.CategoryFragment
import com.example.dainv.mymarket.ui.main.item.marked.ItemsMarkedFragment
import com.example.dainv.mymarket.ui.main.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home ->{
                    replaceFragment(CategoryFragment.newInstance(),CategoryFragment.TAG)
                }
                R.id.menu_profile->{
                    replaceFragment(ProfileFragment.newInstance(),ProfileFragment.TAG)
                }
                R.id.menu_mark->{
                    replaceFragment(ItemsMarkedFragment.newInstance(),ItemsMarkedFragment::class.java.canonicalName)
                }
            }
            true
        }
        replaceFragment(CategoryFragment.newInstance(),"")
        floatBtnAdd.setOnClickListener{
            startActivity(Intent(this,AddItemActivity::class.java))
        }
    }
    private fun replaceFragment(fragment: Fragment,tag:String){
        supportFragmentManager.beginTransaction()
                .replace(R.id.viewContainer,fragment,tag)
                .disallowAddToBackStack()
                .commit()
    }
}