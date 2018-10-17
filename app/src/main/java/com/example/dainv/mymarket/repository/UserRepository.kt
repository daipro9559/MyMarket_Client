package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.LoginResponse
import com.example.dainv.mymarket.service.response.RegisterResponse
import com.example.dainv.mymarket.service.UserService
import com.example.dainv.mymarket.service.response.PhoneResponse
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class UserRepository
@Inject
constructor(val userService: UserService,
            val preferenceHelper: SharePreferencHelper) : BaseRepository() {
    public fun login(email: String, password: String) = object : LoadData<LoginResponse, LoginResponse>() {
        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun processResponse(apiResponse: ApiResponse<LoginResponse>): LoginResponse?{
            val body = apiResponse.body
            if (body!!.success && body!!.data.token != null){
                preferenceHelper.putString(Constant.TOKEN,body!!.data.token)
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
            return apiResponse?.body!!
        }

        override fun getCallService(): LiveData<ApiResponse<RegisterResponse>> {
            return userService.register(email, password, phone, name)
        }

    }.resultData

    fun getPhoneSeller(userID:Int) = object : LoadData<String,PhoneResponse>(){
        override fun processResponse(apiResponse: ApiResponse<PhoneResponse>): String? {
            return apiResponse?.body?.data?.phone
        }
        override fun loadFromDB(): LiveData<String> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        override fun getCallService() = userService.getPhoneNumber(preferenceHelper.getString(Constant.TOKEN,null),userID)
    }.resultData

}