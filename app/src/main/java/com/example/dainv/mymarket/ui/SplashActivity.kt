package com.example.dainv.mymarket.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.util.SharePreferencHelper
import com.example.dainv.mymarket.ui.login.LoginActivity
import com.example.dainv.mymarket.ui.main.MainActivity
import javax.inject.Inject
import android.os.Build
import android.view.WindowManager


class SplashActivity : BaseActivity(){
    @Inject lateinit var sharePreferencHelper: SharePreferencHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        val currentApiVersion = android.os.Build.VERSION.SDK_INT
//
//        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//
//        // This work only for android 4.4+
//        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
//
//            window.decorView.systemUiVisibility = flags
//
//            // Code below is to handle presses of Volume up or Volume down.
//            // Without this, after pressing volume buttons, the navigation bar will
//            // show up and won't hide
//            val decorView = window.decorView
//            decorView
//                    .setOnSystemUiVisibilityChangeListener { visibility ->
//                        if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
//                            decorView.systemUiVisibility = flags
//                        }
//                    }
//        }
        val myIntent :Intent? = if (sharePreferencHelper.getString(Constant.TOKEN,null)!=null){
            Intent(applicationContext,MainActivity::class.java)
        }else {
            Intent(applicationContext,
                    LoginActivity::class.java)
        }
        startActivity(myIntent)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        finish()
    }

    override fun onResume() {
        super.onResume()
//        fullScreen()
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}