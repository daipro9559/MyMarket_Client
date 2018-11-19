package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.items.*
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ListItemContributes {
    @ContributesAndroidInjector
    abstract fun filterFragment(): FilterFragment

    @Binds
    abstract fun listItemView(view: ListItemActivity): ListItemView
    @Binds
    abstract fun presenter(listItemPresenterImp: ListItemPresenterImp): ListItemPresenter
}
