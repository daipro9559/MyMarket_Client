package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.itemdetail.FragmentImage
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ItemDetailActivityModule {
    @ContributesAndroidInjector
    abstract fun fragmentImage(): FragmentImage
}
