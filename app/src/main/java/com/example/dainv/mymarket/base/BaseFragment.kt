package com.example.dainv.mymarket.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.dainv.mymarket.util.MyViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment : Fragment(){
    @Inject
    protected lateinit var viewModelFactory: MyViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}