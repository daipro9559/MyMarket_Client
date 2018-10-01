package com.example.dainv.mymarket.view.register

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.R
import jp.wasabeef.blurry.Blurry
import jp.wasabeef.blurry.internal.Blur
import kotlinx.android.synthetic.main.activity_register.*
import timber.log.Timber


class RegisterActivity : BaseActivity() {
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerViewModel = ViewModelProviders.of(this,viewModelFactory)[RegisterViewModel::class.java]
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        btnRegister.setOnClickListener {
            registerViewModel.register(edtAccount.text.toString().trim(),
                    edtPassword.text.toString().trim(),
                    edtPhone.text.toString(),
                    edtName.text.toString().trim())
        }
        registerViewModel.registerResult.observe(this, Observer {
            it
        })
    }
}