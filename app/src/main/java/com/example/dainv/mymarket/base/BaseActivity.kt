package com.example.dainv.mymarket.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.dainv.mymarket.ui.login.LoginActivity
import com.example.dainv.mymarket.util.MyViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(),HasSupportFragmentInjector{
    @Inject
    lateinit var viewModelFactory: MyViewModelFactory
    @Inject lateinit var  dispatchingAndroidInjector:  DispatchingAndroidInjector<Fragment>
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
    override fun supportFragmentInjector(): AndroidInjector<Fragment>  = dispatchingAndroidInjector

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun enableHomeBack(){
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    public fun unAuthorize(){
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

//    protected abstract fun hasFragmentInjectAble(): Boolean
}
