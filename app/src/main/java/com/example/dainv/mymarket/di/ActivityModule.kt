package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.view.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun loginActivityContribute(): LoginActivity
}