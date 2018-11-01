package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.items.FilterFragment
import com.example.dainv.mymarket.ui.items.ListItemFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ListItemContributes {
    @ContributesAndroidInjector
    abstract fun filterFragment(): FilterFragment

    @ContributesAndroidInjector
    abstract fun listItemFragment(): ListItemFragment
}
