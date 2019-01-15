package com.example.dainv.mymarket.ui.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {
    private val loginParam = MutableLiveData<LoginParam>()
    val loginResult = Transformations.switchMap(loginParam) {
        return@switchMap userRepository.login(it.email, it.password)
    }
    private val forgotTrigger = MutableLiveData<String>()
    val forgotResult = Transformations.switchMap(forgotTrigger) {
        userRepository.forgot(it)
    }
    private val changePassByCodeTrigger = MutableLiveData<ChangePassByCodeParam>()
    val changePassByCodeResult = Transformations.switchMap(changePassByCodeTrigger) {
        userRepository.changePassByCode(it.code, it.password)
    }

    fun login(email: String, password: String) {
        loginParam.value = LoginParam(email, password)
    }

    fun forgot(email: String) {
        forgotTrigger.value = email
    }

    fun changePassByCode(code: Long, newPassword: String) {
        changePassByCodeTrigger.value = ChangePassByCodeParam(code, newPassword)
    }

    class LoginParam(val email: String, val password: String)
    class ChangePassByCodeParam(val code: Long, val password: String)
}