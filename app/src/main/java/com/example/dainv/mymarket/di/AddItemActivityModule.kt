package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.additem.AddItemActivityRouter
import com.example.dainv.mymarket.ui.additem.ImageSelectedAdapter
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
class AddItemActivityModule {

    @Provides
    fun addItemActivityRouter(): AddItemActivityRouter {
        return AddItemActivityRouter()
    }
    @Provides
    fun ImageAdapter(): ImageSelectedAdapter {
        return ImageSelectedAdapter()
    }
}
