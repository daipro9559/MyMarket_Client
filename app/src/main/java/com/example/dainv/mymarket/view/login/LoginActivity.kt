package com.example.dainv.mymarket.view.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.view.register.RegisterActivity
import dagger.android.AndroidInjector
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import timber.log.Timber

class LoginActivity : BaseActivity() {
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        KeyboardVisibilityEvent.setEventListener(this) {
            if (it) {
                view1.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, scrollView.height/4)
            } else {
                view1.visibility = View.GONE
                scrollView.invalidate()
                scrollView.smoothScrollTo(0, 0)
            }
        }
        loginViewModel = ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        btnLogin.setOnClickListener {
            loginViewModel.login(edtAccount.text.toString().trim(),
                    edtPassword.text.toString().trim())
        }
        loginViewModel.loginResult.observe(this, Observer {
            if (it!!.r !=null) {
               Timber.e(it.r!!.token)
            }
        })
        btnRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}