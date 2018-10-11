package com.example.dainv.mymarket.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.dainv.mymarket.util.MyViewModelFactory
import com.example.dainv.mymarket.view.login.LoginViewModel
import com.example.dainv.mymarket.view.main.category.CategoryViewModel
import com.example.dainv.mymarket.view.register.RegisterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun registerViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun categoryViewModel(categoryViewModel: CategoryViewModel):ViewModel

    @Binds
    abstract fun viewModelKey(myViewModelFactory: MyViewModelFactory): ViewModelProvider.Factory
}