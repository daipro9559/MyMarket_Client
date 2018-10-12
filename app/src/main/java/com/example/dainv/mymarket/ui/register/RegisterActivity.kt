package com.example.dainv.mymarket.ui.register


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.model.ResourceState
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : BaseActivity() {
    @Inject
    lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        btnRegister.setOnClickListener {
            if (edtPassword.text.toString() != edtConfirmPassword.text.toString()){
                edtConfirmPassword.error = getString(R.string.confirm_pass_not_same)
                return@setOnClickListener
            }
            registerViewModel.register(edtAccount.text.toString(),
                    edtPassword.text.toString(),
                    edtPhone.text.toString(),
                    edtName.text.toString())
        }
        registerViewModel.registerResult.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING){
            }else if (it.r!=null && it!!.r!!.success){
                val email = it!!.r!!.user.email
                val intent = Intent()
                intent.putExtra("email",email)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        })
    }
}