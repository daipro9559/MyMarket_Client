package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.main.category.CategoryFragment
import com.example.dainv.mymarket.ui.main.item.marked.ItemsMarkedFragment
import com.example.dainv.mymarket.ui.main.notifications.NotificationFragment
import com.example.dainv.mymarket.ui.main.profile.ProfileFragment
import com.example.dainv.mymarket.ui.main.stands.StandsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainBuilderFragmentModule {
    @ContributesAndroidInjector
    abstract fun categoryFragment(): CategoryFragment

    @ContributesAndroidInjector
    abstract fun profileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun itemMarkedFragment():ItemsMarkedFragment

    @ContributesAndroidInjector
    abstract fun standsFragment(): StandsFragment


    @ContributesAndroidInjector
    abstract fun notificationFragment(): NotificationFragment
}
