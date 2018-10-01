package com.example.dainv.mymarket

import android.app.Activity
import android.app.Application
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class MyMarketApp : Application(), HasActivityInjector{
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): AndroidInjector<Activity> {
       return dispatchingAndroidInjector
    }
    override fun onCreate() {
        super.onCreate()
        if (Constant.IS_DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        DaggerAppComponent.builder().application(this).build().inject(this)
    }
}