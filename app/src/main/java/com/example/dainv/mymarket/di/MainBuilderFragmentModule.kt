package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.main.category.CategoryFragment
import com.example.dainv.mymarket.ui.main.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainBuilderFragmentModule {
    @ContributesAndroidInjector
    abstract fun categoryFragment(): CategoryFragment

    @ContributesAndroidInjector
    abstract fun profileFragment(): ProfileFragment
}
