package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.LoginResponse
import com.example.dainv.mymarket.api.response.RegisterResponse
import com.example.dainv.mymarket.api.UserService
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.PhoneResponse
import com.example.dainv.mymarket.api.response.ProfileResponse
import com.example.dainv.mymarket.model.User
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class UserRepository
@Inject
constructor(val userService: UserService,
             preferenceHelper: SharePreferencHelper) : BaseRepository(preferenceHelper) {
     fun login(email: String, password: String) = object : LoadData<LoginResponse, LoginResponse>() {
        override fun processResponse(apiResponse: ApiResponse<LoginResponse>): LoginResponse? {
            val body = apiResponse.body
            body?.let {
                if (body!!.success && body!!.data.token != null) {
                    sharePreferencHelper.putString(Constant.TOKEN, body.data.token)
                    sharePreferencHelper.putInt(Constant.USER_TYPE,body.data.user.userType)
                }
                return@processResponse body
            }
            return null
        }

        override fun getCallService(): LiveData<ApiResponse<LoginResponse>> {
            return userService.login(email, password)
        }

    }.getLiveData()

     fun register(email: String, password: String, phone: String, name: String) = object : LoadData<RegisterResponse, RegisterResponse>() {
        override fun processResponse(apiResponse: ApiResponse<RegisterResponse>): RegisterResponse? {
            return apiResponse?.body!!
        }

        override fun getCallService(): LiveData<ApiResponse<RegisterResponse>> {
            return userService.register(email, password, phone, name)
        }
    }.getLiveData()

    fun getPhoneSeller(userID: String) = object : LoadData<String, PhoneResponse>() {
        override fun processResponse(apiResponse: ApiResponse<PhoneResponse>): String? {
            return apiResponse?.body?.data?.phone
        }
        override fun getCallService() = userService.getPhoneNumber(token, userID)
    }.getLiveData()


    fun getProfile() = object : LoadData<User, ProfileResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ProfileResponse>): User? {
            return apiResponse.body!!.data
        }

        override fun getCallService() = userService.getProfile(token)

    }.getLiveData()

    fun updateToSeller() = object :LoadData<Boolean,BaseResponse>(){
        override fun getCallService(): LiveData<ApiResponse<BaseResponse>> = userService.updateToSeller(token)

        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            if(apiResponse?.body?.success!!){
                sharePreferencHelper.putInt(Constant.USER_TYPE,1) // save to seller
            }
         return   apiResponse?.body?.success
        }

    }.getLiveData()
}