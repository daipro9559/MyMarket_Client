package com.example.dainv.mymarket.di

import android.arch.lifecycle.ViewModel
import kotlin.reflect.KClass

@MustBeDocumented
@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
annotation class ViewModelKey(
        val value: KClass<out ViewModel>
)