package com.example.dainv.mymarket.ui.login

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity

import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.addAddress.AddAddressActivity
import com.example.dainv.mymarket.ui.admin.MainAdminActivity
import com.example.dainv.mymarket.ui.main.MainActivity
import com.example.dainv.mymarket.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

const val LOGIN_REQUEST_CODE = 100

class LoginActivity : BaseActivity() {
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        KeyboardVisibilityEvent.setEventListener(this) {
            if (it) {
                view1.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, scrollView.height / 3)
            } else {
                view1.visibility = View.GONE
                scrollView.invalidate()
                scrollView.smoothScrollTo(0, 0)
            }
        }
        loginViewModel = ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        btnLogin.setOnClickListener {
            if (edtAccount.text.isNullOrEmpty()) {
                edtAccount.error = getString(R.string.email_require)
                return@setOnClickListener
            }
            if (edtPassword.text.isNullOrEmpty()) {
                edtPassword.error = getString(R.string.password_require)
                return@setOnClickListener
            }
            loginViewModel.login(edtAccount.text.toString().trim(),
                    edtPassword.text.toString().trim())
        }
        btnRegister.setOnClickListener {
            startActivityForResult(Intent(this, RegisterActivity::class.java), LOGIN_REQUEST_CODE)
        }
        loginViewModel.loginResult.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
            if (it!!.r != null && it!!.r!!.success) {
                if (it.r!!.data.user.userRoleID == 2) {
                    if (it?.r!!.data.user.Address == null) {
                        startActivityWithAnimation(Intent(this, AddAddressActivity::class.java))
                        finish()
                    }else {
                        startActivityWithAnimation(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    startActivityWithAnimation(Intent(this, MainAdminActivity::class.java))
                    finish()
                }
            } else if (it!!.r != null && !it.r!!.success) {
                Toast.makeText(this, it!!.r!!.message, Toast.LENGTH_LONG).show()
            }
        })

        loginViewModel.forgotResult.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
                if(it.resourceState == ResourceState.SUCCESS){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.notification)
                            .setMessage(R.string.check_mail_to_get_code)
                            .setPositiveButton(R.string.confirm) { dialog, which ->
                                dialog.cancel()
                            }

                            .create()
                    builder.show()
                }
            }
        })
        loginViewModel.changePassByCodeResult.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
                if(it.resourceState == ResourceState.SUCCESS){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.notification)
                            .setMessage(R.string.change_pass_completed)
                            .setPositiveButton(R.string.confirm) { dialog, which ->
                                dialog.cancel()
                            }
                            .create()
                    builder.show()
                }
            }
        })

        txtForgotPassword.setOnClickListener {
            showDialogQuestion()
        }
    }

    private fun showDialogQuestion(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.notification)
                .setMessage(R.string.you_have_code)
                .setPositiveButton(R.string.haved) { dialog, which ->
                    dialog.cancel()
                    showDialogChangePassByCode()
                }
                .setNegativeButton(R.string.not_have){ dialog, which ->
                    dialog.cancel()
                    showDialogInputEmail()
                }
                .create()
        builder.show()
    }
    private fun showDialogInputEmail(){
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.input_email,null,false)
        val edtEmail = view.findViewById<AppCompatEditText>(R.id.editEmail)
        val alertDialog = builder.setTitle(R.string.input_email)
                .setView(view)
                .setPositiveButton(R.string.confirm) { dialog, which ->
                    if (edtEmail.text.isNullOrEmpty()){
                        edtEmail.error = getString(R.string.not_empty)
                    }else {
                        loginViewModel.forgot(edtEmail.text.toString())
                        dialog.cancel()
                    }
                }
                .setNegativeButton(R.string.cancel){ dialog, which ->
                    dialog.cancel()
                }
                .create()
        alertDialog.getWindow().setLayout(600, 400)
        alertDialog.show()
    }

    private fun showDialogChangePassByCode(){
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.change_pass_by_code,null,false)
        val edtCode = view.findViewById<AppCompatEditText>(R.id.edtCode)
        val edtPass = view.findViewById<AppCompatEditText>(R.id.edtPass)
        val alertDialog =  builder.setTitle(R.string.change_pass)
                .setView(view)
                .setPositiveButton(R.string.confirm) { dialog, which ->
                    var isPass  = true
                    if (edtCode.text.isNullOrEmpty()){
                        edtCode.error = getString(R.string.not_empty)
                        isPass = false
                    }
                    if (edtPass.text.isNullOrEmpty()){
                        edtPass.error = getString(R.string.not_empty)
                        isPass = false

                    }
                   if(isPass) {
                        loginViewModel.changePassByCode(edtCode.text.toString().toLong(),edtPass.text.toString())
                        dialog.cancel()
                    }
                }
                .setNegativeButton(R.string.cancel){ dialog, which ->
                    dialog.cancel()
                }
                .create()
        alertDialog.getWindow().setLayout(600, 400)

        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val email = data!!.getStringExtra("email")
            edtAccount.setText(email)
        }
    }

}