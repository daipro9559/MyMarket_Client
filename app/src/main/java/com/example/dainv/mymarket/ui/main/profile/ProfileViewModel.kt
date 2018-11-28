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
    private val changePassParam = MutableLiveData<ChangePassParam>()
    val profileLiveData  =  Transformations.switchMap(profileTrigger){
        return@switchMap userRepository.getMyProfile()
    }
    val logoutResult = Transformations.switchMap(logoutTrigger){
        return@switchMap userRepository.logout()
    }
    val changePassResultLiveData = Transformations.switchMap(changePassParam){
        userRepository.changePassword(it.oldPass,it.newPass)
    }!!


    fun getProfile(){
        profileTrigger.value = ""
    }

    fun logout(){
        logoutTrigger.value = Any()
    }
    fun changPass(oldPass:String,newPass:String){
        changePassParam.value = ChangePassParam(oldPass,newPass)
    }

   data class ChangePassParam(
        val oldPass:String,
        val newPass:String    )
}