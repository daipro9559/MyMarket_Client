package com.example.dainv.mymarket.di

import android.arch.lifecycle.ViewModelProvider
import com.example.dainv.mymarket.base.MyViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {


    @Binds
    public abstract fun viewModelKey(myViewModelFactory: MyViewModelFactory): ViewModelProvider.Factory
}