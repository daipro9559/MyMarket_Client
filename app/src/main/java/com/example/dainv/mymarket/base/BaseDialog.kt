package com.example.dainv.mymarket.base

import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.example.dainv.mymarket.util.MyViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseDialog : DialogFragment(){
    @Inject protected lateinit var viewModelFactory: MyViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}