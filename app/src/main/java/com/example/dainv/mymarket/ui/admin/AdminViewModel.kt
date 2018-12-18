package com.example.dainv.mymarket.ui.admin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class AdminViewModel
    @Inject
    constructor(private val userRepository: UserRepository)
    : ViewModel(){
    private val pageTrigger = MutableLiveData<Int>()

    val listUserResult = Transformations.switchMap(pageTrigger){
        userRepository.getUsers(it)
    }

    fun getUsers(page:Int){
        pageTrigger.value = page
    }
}