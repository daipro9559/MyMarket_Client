package com.example.dainv.mymarket.ui.admin

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.entity.Category
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.ui.items.ListItemActivity
import com.example.dainv.mymarket.ui.login.LoginActivity
import com.example.dainv.mymarket.util.SharePreferencHelper
import kotlinx.android.synthetic.main.activity_main_admin.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class MainAdminActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var adminViewModel: AdminViewModel
    @Inject
    lateinit var sharePreferencHelper: SharePreferencHelper
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
        adminViewModel.logoutResult.observe(this, Observer {
            if(it!!.resourceState == ResourceState.SUCCESS) {
                sharePreferencHelper.putString(Constant.TOKEN, null)
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }

        })
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
        // Handle action bar item_view_pager clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         return  super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item_view_pager clicks here.
        when (item.itemId) {
            R.id.user_manage -> {
                setTitle(R.string.user_manager)
                supportFragmentManager.beginTransaction().replace(R.id.viewContainer,UserManagerFragment.newInstance(),UserManagerFragment.javaClass.name)
                        .commit()
            }
            R.id.item_manager -> {
                val intent = Intent(this, ListItemActivity::class.java)
                intent.action = ListItemActivity.ACTION_MANAGER_ADMIN
                val bundle = Bundle()
                bundle.putParcelable(ListItemActivity.CATEGORY_KEY,Category(1,"Đồ điện tử,công nghệ",""))
                intent.putExtra("bundle",bundle)
                startActivityWithAnimation(intent)
            }
            R.id.admin_logout->{
                adminViewModel.logout()

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
