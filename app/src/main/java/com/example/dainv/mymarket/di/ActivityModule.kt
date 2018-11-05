package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.ImageActivity
import com.example.dainv.mymarket.ui.SplashActivity
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import com.example.dainv.mymarket.ui.items.ListItemActivity
import com.example.dainv.mymarket.ui.login.LoginActivity
import com.example.dainv.mymarket.ui.main.MainActivity
import com.example.dainv.mymarket.ui.register.RegisterActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun loginActivityContribute(): LoginActivity

    @ContributesAndroidInjector
    abstract fun registerActivity(): RegisterActivity

    @ContributesAndroidInjector(modules = [MainBuilderFragmentModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [AddItemActivityModule::class, AddItemActivityContributesModule::class])
    abstract fun addItemActivity(): AddItemActivity

    @ContributesAndroidInjector(modules = [ListItemContributes::class])
    abstract fun itemsActivity(): ListItemActivity

    @ContributesAndroidInjector
    abstract fun itemDetailActivity(): ItemDetailActivity

    @ContributesAndroidInjector
    abstract fun imageActivity(): ImageActivity

    @ContributesAndroidInjector
    abstract fun dialogSelectCategory(): DialogSelectCategory

    @ContributesAndroidInjector
    abstract fun dialogSelectProvince(): DialogSelectProvince

    @ContributesAndroidInjector
    abstract fun dialogSelectDistrict(): DialogSelectDistrict
}