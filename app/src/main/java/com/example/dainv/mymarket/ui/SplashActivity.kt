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
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import com.google.firebase.iid.FirebaseInstanceId
import timber.log.Timber


class SplashActivity : BaseActivity(){
    @Inject lateinit var sharePreferencHelper: SharePreferencHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        fullScreen()
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
        if(intent.extras ==null) {
            // TODO get token firebase for test
            val tokenResult = FirebaseInstanceId.getInstance().instanceId
            tokenResult.addOnCompleteListener {
                it.result?.token?.let { token ->
                    Timber.e(token)
                }
            }
            val myIntent: Intent? = if (sharePreferencHelper.getString(Constant.TOKEN, null) != null) {
                Intent(applicationContext, MainActivity::class.java)
            } else {
                Intent(applicationContext,
                        LoginActivity::class.java)
            }
            startActivity(myIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }else{
            val extras = intent.extras
            val  intentItemDetail = Intent(this, ItemDetailActivity::class.java)
            intentItemDetail.putExtra("itemID",extras.getString("itemID"))
            intentItemDetail.putExtra("standID",extras.getString("standID"))
            intentItemDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intentItemDetail.action = ItemDetailActivity.ACTION_SHOW_FROM_ID
            startActivity(intentItemDetail)
            finish()

        }
    }

    override fun onResume() {
        super.onResume()
//        fullScreen()
    }

    private fun fullScreen() {
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