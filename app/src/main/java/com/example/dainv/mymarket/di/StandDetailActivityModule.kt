package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.ui.stand.detail.DialogWriteComment
import com.example.dainv.mymarket.ui.stand.detail.StandDetailFragment
import com.example.dainv.mymarket.ui.stand.detail.StandInformationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class StandDetailActivityModule {
    @ContributesAndroidInjector
    abstract fun standInforFragment():StandInformationFragment
    @ContributesAndroidInjector
    abstract fun standDetailFragment():StandDetailFragment

    @ContributesAndroidInjector
    abstract fun dialogWriteComment():DialogWriteComment
}
