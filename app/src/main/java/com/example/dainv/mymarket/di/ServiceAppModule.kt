package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.service.FireBaseService
import com.example.dainv.mymarket.service.UploadService
import dagger.Module
import dagger.android.ContributesAndroidInjector
@Module
abstract class ServiceAppModule {
    @ContributesAndroidInjector
    abstract fun uploadServiceContributed():UploadService

    @ContributesAndroidInjector
    abstract fun firebaseService():FireBaseService
}