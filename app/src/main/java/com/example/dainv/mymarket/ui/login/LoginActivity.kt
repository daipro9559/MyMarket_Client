package com.example.dainv.mymarket.ui.login

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.R.id.btnLogin
import com.example.dainv.mymarket.base.BaseActivity

import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.admin.MainAdminActivity
import com.example.dainv.mymarket.ui.main.MainActivity
import com.example.dainv.mymarket.ui.register.RegisterActivity
import dagger.android.AndroidInjector
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import timber.log.Timber
const val LOGIN_REQUEST_CODE = 100
class LoginActivity : BaseActivity() {
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        KeyboardVisibilityEvent.setEventListener(this) {
            if (it) {
                view1.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, scrollView.height/3)
            } else {
                view1.visibility = View.GONE
                scrollView.invalidate()
                scrollView.smoothScrollTo(0, 0)
            }
        }
        loginViewModel = ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        btnLogin.setOnClickListener {
            if(edtAccount.text.isNullOrEmpty()){
                edtAccount.error = getString(R.string.email_require)
                return@setOnClickListener
            }
            if (edtPassword.text.isNullOrEmpty()){
                edtPassword.error = getString(R.string.password_require)
                return@setOnClickListener
            }
            loginViewModel.login(edtAccount.text.toString().trim(),
                    edtPassword.text.toString().trim())
        }
        btnRegister.setOnClickListener {
            startActivityForResult(Intent(this,RegisterActivity::class.java), LOGIN_REQUEST_CODE)
        }
        loginViewModel.loginResult.observe(this, Observer {
            if (it!!.resourceState ==ResourceState.LOADING) {
                progressBar.visibility = View.VISIBLE
            }else{
                progressBar.visibility = View.GONE
            }
            if (it!!.r !=null && it!!.r!!.success){
                if(it.r!!.data.user.userRoleID == 2) {
                    startActivityWithAnimation(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    startActivityWithAnimation(Intent(this, MainAdminActivity::class.java))
                    finish()
                }
            }else if (it!!.r !=null && !it.r!!.success){
                Toast.makeText(this,it!!.r!!.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            val email = data!!.getStringExtra("email")
            edtAccount.setText(email)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }

}