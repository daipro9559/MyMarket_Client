package com.example.dainv.mymarket.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.util.SharePreferencHelper
import com.example.dainv.mymarket.view.login.LoginActivity
import com.example.dainv.mymarket.view.main.MainActivity
import javax.inject.Inject

class SplashActivity : BaseActivity(){
    @Inject lateinit var sharePreferencHelper: SharePreferencHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val decorView = window.decorView
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        val myIntent :Intent?
        if (sharePreferencHelper.getString(Constant.TOKEN,null)!=null){
            myIntent = Intent(applicationContext,MainActivity::class.java)
        }else {
            myIntent = Intent(applicationContext,
                    LoginActivity::class.java)
        }
        startActivity(myIntent)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        finish()
    }
}