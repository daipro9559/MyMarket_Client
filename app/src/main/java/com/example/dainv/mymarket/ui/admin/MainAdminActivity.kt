package com.example.dainv.mymarket.ui.admin

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main_admin.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainAdminActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var adminViewModel: AdminViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        adminViewModel = ViewModelProviders.of(this,viewModelFactory)[AdminViewModel::class.java]
        supportFragmentManager.beginTransaction().replace(R.id.viewContainer,UserManagerFragment.newInstance(),UserManagerFragment.javaClass.name)
                .commit()
        setTitle(R.string.user_manager)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         return  super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.user_manage -> {
                setTitle(R.string.user_manager)
                supportFragmentManager.beginTransaction().replace(R.id.viewContainer,UserManagerFragment.newInstance(),UserManagerFragment.javaClass.name)
                        .commit()
            }
            R.id.item_manager -> {
                setTitle(R.string.item_manager)

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
