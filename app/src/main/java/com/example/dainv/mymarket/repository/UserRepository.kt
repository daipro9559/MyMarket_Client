package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.LoginResponse
import com.example.dainv.mymarket.api.response.RegisterResponse
import com.example.dainv.mymarket.api.UserService
import com.example.dainv.mymarket.api.response.PhoneResponse
import com.example.dainv.mymarket.api.response.ProfileResponse
import com.example.dainv.mymarket.model.User
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
            body?.let {
                if (body!!.success && body!!.data.token != null){
                    preferenceHelper.putString(Constant.TOKEN,body!!.data.token)
                }
                return@processResponse body
            }
            return null

        }

        override fun loadFromDB(): LiveData<LoginResponse> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override fun getCallService(): LiveData<ApiResponse<LoginResponse>> {
            return userService.login(email, password)
        }

    }.getLiveData()

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

    }.getLiveData()

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
    }.getLiveData()


    fun getProfile() = object :LoadData<User,ProfileResponse>(){
        override fun processResponse(apiResponse: ApiResponse<ProfileResponse>): User? {
            return apiResponse.body!!.data
        }

        override fun loadFromDB(): LiveData<User> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCallService() = userService.getProfile(preferenceHelper.getString(Constant.TOKEN,null))

    }.getLiveData()

}