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
    private val profileTrigger = MutableLiveData<Any>()
    val profileLiveData  =  Transformations.switchMap(profileTrigger){
        return@switchMap userRepository.getProfile()
    }

    fun getProfile(){
        profileTrigger.value = ""
    }
}