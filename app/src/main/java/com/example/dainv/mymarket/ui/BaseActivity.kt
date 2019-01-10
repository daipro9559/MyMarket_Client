package com.example.dainv.mymarket.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.entity.ErrorResponse
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.login.LoginActivity
import com.example.dainv.mymarket.util.MyViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import android.support.v4.content.IntentCompat
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK



abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var viewModelFactory: MyViewModelFactory
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun enableHomeBack() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun handelErrResponseApi(errorResponse: ErrorResponse) {
        if (errorResponse == ErrorResponse.UN_AUTHORIZED) {
            unAuthorize()
        }
    }

    private fun unAuthorize() {
        showDialogTokenExpire()
    }

    protected fun viewLoading(resourceState: ResourceState, view: View) {
        if (resourceState == ResourceState.LOADING) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    fun startActivityWithAnimation(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun transitionOut() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        transitionOut()
    }

    private fun showDialogTokenExpire() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.notification)
                .setMessage(R.string.login_expire)
                .setPositiveButton(R.string.confirm) { dialog, which ->
                    dialog.cancel()
                    // goto login activity
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                .create()
        builder.show()
    }

//    protected abstract fun hasFragmentInjectAble(): Boolean
}
