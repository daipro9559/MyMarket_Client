package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.admin.ItemManagerFragment
import com.example.dainv.mymarket.ui.admin.UserManagerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AdminMainActivityModule {
    @ContributesAndroidInjector
    abstract fun userManagerFragment() : UserManagerFragment

    @ContributesAndroidInjector
    abstract fun itemManagerFragment() : ItemManagerFragment
}
