package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.view.login.LoginActivity
import com.example.dainv.mymarket.view.register.RegisterActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun loginActivityContribute(): LoginActivity

    @ContributesAndroidInjector
    abstract fun registerActivity(): RegisterActivity
}