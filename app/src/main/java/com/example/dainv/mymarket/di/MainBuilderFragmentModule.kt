package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.service.ItemService
import com.example.dainv.mymarket.ui.main.category.CategoryFragment
import com.example.dainv.mymarket.ui.main.profile.ProfileFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class MainBuilderFragmentModule {
    @ContributesAndroidInjector
    abstract fun categoryFragment(): CategoryFragment

    @ContributesAndroidInjector
    abstract fun profileFragment(): ProfileFragment
}
