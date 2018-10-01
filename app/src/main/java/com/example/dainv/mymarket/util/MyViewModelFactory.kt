package com.example.dainv.mymarket.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

public class MyViewModelFactory @Inject constructor(var creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>)
    : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators[modelClass] ?: creators.entries.firstOrNull{
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalAccessException("dont know this viewModel class")
        try {
            return creator.get() as T
        }catch (e :Exception){
            throw RuntimeException()
        }

    }

}