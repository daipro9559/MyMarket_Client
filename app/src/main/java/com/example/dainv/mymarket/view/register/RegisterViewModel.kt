package com.example.dainv.mymarket.view.register

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class RegisterViewModel
@Inject
constructor(val userRepository: UserRepository): ViewModel(){
    private val registerParam = MutableLiveData<RegisterParam>()
    val registerResult = Transformations.switchMap(registerParam){
        return@switchMap userRepository.register(it.email,it.pass,it.phone,it.name)
    }

data  class RegisterParam(val email:String,val pass:String,val phone:String,val name:String)
}