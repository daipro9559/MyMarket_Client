package com.example.dainv.mymarket.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.dainv.mymarket.ui.additem.AddItemViewModel
import com.example.dainv.mymarket.ui.create.stand.CreateStandViewModel
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailViewModel
import com.example.dainv.mymarket.ui.items.ListItemViewModel
import com.example.dainv.mymarket.util.MyViewModelFactory
import com.example.dainv.mymarket.ui.login.LoginViewModel
import com.example.dainv.mymarket.ui.main.category.CategoryViewModel
import com.example.dainv.mymarket.ui.main.item.marked.ItemsMarkedViewModel
import com.example.dainv.mymarket.ui.main.profile.ProfileViewModel
import com.example.dainv.mymarket.ui.my.stands.MyStandsViewModel
import com.example.dainv.mymarket.ui.register.RegisterViewModel
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
    @IntoMap
    @ViewModelKey(ListItemViewModel::class)
    abstract fun listItemViewModel(addItemViewModel: ListItemViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ItemDetailViewModel::class)
    abstract fun itemDetailViewModel(itemDetailViewModel: ItemDetailViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun profileViewModel(profileViewModel: ProfileViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddItemViewModel::class)
    abstract fun addItemViewModel(addItemViewModel: AddItemViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ItemsMarkedViewModel::class)
    abstract fun itemsMarkedViewModel(itemsMarkedViewModel: ItemsMarkedViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyStandsViewModel::class)
    abstract fun myStandsViewModel(myStandsViewModel: MyStandsViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateStandViewModel::class)
    abstract fun createStandViewModel(createStandViewModel: CreateStandViewModel):ViewModel

    @Binds
    abstract fun viewModelKey(myViewModelFactory: MyViewModelFactory): ViewModelProvider.Factory
}