package com.example.dainv.mymarket.view.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel(){
    private val loginParam = MutableLiveData<LoginParam>()
    val loginResult = Transformations.switchMap(loginParam){
        return@switchMap userRepository.login(it.email,it.password)
    }
    public fun login(email:String, password: String){
        loginParam.value = LoginParam(email,password)
    }

    class LoginParam(val email:String,val password:String)
}