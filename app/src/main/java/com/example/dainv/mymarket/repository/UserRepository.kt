package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.LoginResponse
import com.example.dainv.mymarket.model.RegisterResponse
import com.example.dainv.mymarket.service.UserService
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class UserRepository
@Inject
constructor(val userService: UserService,
            val preferencHelper: SharePreferencHelper) : BaseRepository() {
    public fun login(email: String, password: String) = object : LoadData<LoginResponse, LoginResponse>() {
        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun processResponse(apiResponse: ApiResponse<LoginResponse>): LoginResponse?{
            val body = apiResponse.body
            if (body!!.success && body!!.data.token != null){
                preferencHelper.putString(Constant.TOKEN,body!!.data.token)
            }
            return apiResponse.body
        }

        override fun loadFromDB(): LiveData<LoginResponse> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override fun getCallService(): LiveData<ApiResponse<LoginResponse>> {
            return userService.login(email, password)
        }

    }.resultData

    public fun register(email: String, password: String, phone: String, name: String) = object : LoadData<RegisterResponse, RegisterResponse>() {
        override fun loadFromDB(): LiveData<RegisterResponse> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun processResponse(apiResponse: ApiResponse<RegisterResponse>): RegisterResponse?{
            return apiResponse.body!!
        }

        override fun getCallService(): LiveData<ApiResponse<RegisterResponse>> {
            return userService.register(email, password, phone, name)
        }

    }.resultData
}