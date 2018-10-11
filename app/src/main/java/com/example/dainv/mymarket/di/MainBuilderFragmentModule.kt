package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.view.main.category.CategoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainBuilderFragmentModule {
    @ContributesAndroidInjector
    abstract fun categoryFragment(categoryFragment: CategoryFragment)
}
