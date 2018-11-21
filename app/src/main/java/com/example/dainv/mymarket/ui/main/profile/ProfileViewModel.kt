package com.example.dainv.mymarket.ui.main.profile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class ProfileViewModel
    @Inject
    constructor(
            userRepository: UserRepository
    )
    :ViewModel(){
    private val logoutTrigger = MutableLiveData<Any>()
    private val profileTrigger = MutableLiveData<Any>()
    val profileLiveData  =  Transformations.switchMap(profileTrigger){
        return@switchMap userRepository.getMyProfile()
    }
    val logoutResult = Transformations.switchMap(logoutTrigger){
        return@switchMap userRepository.logout()
    }

    fun getProfile(){
        profileTrigger.value = ""
    }

    fun logout(){
        logoutTrigger.value = Any()
    }
}