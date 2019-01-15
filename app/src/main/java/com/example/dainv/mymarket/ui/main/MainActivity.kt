package com.example.dainv.mymarket.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import com.example.dainv.mymarket.ui.main.category.CategoryFragment
import com.example.dainv.mymarket.ui.main.notifications.NotificationFragment
import com.example.dainv.mymarket.ui.main.profile.ProfileFragment
import com.example.dainv.mymarket.ui.main.stands.StandsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home ->{
//                    floatBtnAdd.show()
                    replaceFragment(CategoryFragment.newInstance(),CategoryFragment.TAG)
                }
                R.id.menu_profile->{
//                    floatBtnAdd.show()
                    replaceFragment(ProfileFragment.newInstance(),ProfileFragment.TAG)
                }
                R.id.menu_notification->{
//                    floatBtnAdd.show()
                    replaceFragment(NotificationFragment.newInstance(),NotificationFragment::class.java.canonicalName)
                }
                R.id.menu_stand->{
//                    floatBtnAdd.hide()
                    replaceFragment(StandsFragment.newInstance(),StandsFragment::class.java.canonicalName)
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
    override fun onBackPressed() {
        super.onBackPressed()
    }
}