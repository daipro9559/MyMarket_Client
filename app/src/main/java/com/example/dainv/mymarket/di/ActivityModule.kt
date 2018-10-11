package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.view.SplashActivity
import com.example.dainv.mymarket.view.login.LoginActivity
import com.example.dainv.mymarket.view.main.MainActivity
import com.example.dainv.mymarket.view.register.RegisterActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun loginActivityContribute(): LoginActivity

    @ContributesAndroidInjector
    abstract fun registerActivity(): RegisterActivity

    @ContributesAndroidInjector(modules = [MainBuilderFragmentModule::class])
    abstract fun mainActivity(): MainActivity

}