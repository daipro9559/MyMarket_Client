package com.example.dainv.mymarket

import android.app.Activity
import android.app.Application
import android.app.Service
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import timber.log.Timber
import javax.inject.Inject

class MyMarketApp : Application(), HasActivityInjector,HasServiceInjector{
    override fun serviceInjector()= dispatchingService
    @Inject
    lateinit var dispatchingService: DispatchingAndroidInjector<Service>
    @Inject
    lateinit var dispatchingActivity: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): AndroidInjector<Activity> {
       return dispatchingActivity
    }
    override fun onCreate() {
        super.onCreate()
        if (Constant.IS_DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        DaggerAppComponent.builder().application(this).build().inject(this)
    }
}