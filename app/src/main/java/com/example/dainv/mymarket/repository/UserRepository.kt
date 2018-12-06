package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.LoginResponse
import com.example.dainv.mymarket.api.response.RegisterResponse
import com.example.dainv.mymarket.api.UserService
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.PhoneResponse
import com.example.dainv.mymarket.api.response.ProfileResponse
import com.example.dainv.mymarket.model.ResourceWrapper
import com.example.dainv.mymarket.model.User
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

class UserRepository
@Inject
constructor(val userService: UserService,
            preferenceHelper: SharePreferencHelper) : BaseRepository(preferenceHelper) {
    fun login(email: String, password: String): LiveData<ResourceWrapper<LoginResponse?>> {
        val mediaLiveData = MediatorLiveData<ResourceWrapper<LoginResponse?>>()
        val tokenFireBase = sharePreferencHelper.getString(Constant.TOKEN_FIREBASE, null)
        if (tokenFireBase.isNullOrEmpty()) {
            val tokenResult = FirebaseInstanceId.getInstance().instanceId
            tokenResult.addOnCompleteListener {
                it.result?.token?.let { token ->
                    // save token firebase
                    sharePreferencHelper.putString(Constant.TOKEN_FIREBASE, token)
                    val liveDataLogin = object : LoadData<LoginResponse, LoginResponse>() {
                        override fun processResponse(apiResponse: ApiResponse<LoginResponse>): LoginResponse? {
                            val body = apiResponse.body
                            body?.let {
                                if (body!!.success && body!!.data.token != null) {
                                    sharePreferencHelper.putString(Constant.TOKEN, body.data.token)
                                    sharePreferencHelper.putString(Constant.USER_ID, body.data.user.userID)
                                    sharePreferencHelper.putInt(Constant.USER_TYPE, body.data.user.userType)
                                }
                                return@processResponse body
                            }
                            return null
                        }

                        override fun getCallService(): LiveData<ApiResponse<LoginResponse>> {
                            return userService.login(email, password, token)
                        }

                    }.getLiveData()
                    mediaLiveData.addSource(liveDataLogin) { loginResource ->
                        mediaLiveData.value = loginResource
                    }
                }
            }
            tokenResult.addOnFailureListener {
                mediaLiveData.value = ResourceWrapper.error("cannot  get token FireBase",it)
            }
        } else {
            val liveDataLogin = object : LoadData<LoginResponse, LoginResponse>() {
                override fun processResponse(apiResponse: ApiResponse<LoginResponse>): LoginResponse? {
                    val body = apiResponse.body
                    body?.let {
                        if (body!!.success && body!!.data.token != null) {
                            sharePreferencHelper.putString(Constant.TOKEN, body.data.token)
                            sharePreferencHelper.putString(Constant.USER_ID, body.data.user.userID)
                            sharePreferencHelper.putInt(Constant.USER_TYPE, body.data.user.userType)
                        }
                        return@processResponse body
                    }
                    return null
                }

                override fun getCallService(): LiveData<ApiResponse<LoginResponse>> {
                    return userService.login(email, password, tokenFireBase!!)
                }

            }.getLiveData()
            mediaLiveData.addSource(liveDataLogin) { loginResource ->
                mediaLiveData.value = loginResource
            }
        }
        return mediaLiveData
    }

    fun register(email: String, password: String, phone: String, name: String) = object : LoadData<RegisterResponse, RegisterResponse>() {
        override fun processResponse(apiResponse: ApiResponse<RegisterResponse>): RegisterResponse? {
            apiResponse?.body?.let {
                return@processResponse it
            }
            return null
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


    fun getMyProfile() = object : LoadData<User, ProfileResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ProfileResponse>): User? {

            return apiResponse?.body?.data
        }

        override fun getCallService() = userService.getMyProfile(token)

    }.getLiveData()

    fun getProfile(userID: String) = object : LoadData<User, ProfileResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ProfileResponse>): User? {

            return apiResponse?.body?.data
        }

        override fun getCallService() = userService.getProfile(token, userID)

    }.getLiveData()

    fun updateToSeller() = object : LoadData<Boolean, BaseResponse>() {
        override fun getCallService(): LiveData<ApiResponse<BaseResponse>> = userService.updateToSeller(token)

        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            if (apiResponse?.body?.success!!) {
                sharePreferencHelper.putInt(Constant.USER_TYPE, 1) // save to seller
            }
            return apiResponse?.body?.success
        }

    }.getLiveData()

    fun logout() = object : LoadData<Boolean, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            return apiResponse?.body?.success
        }

        override fun getCallService() = userService.logout(token)

    }.getLiveData()

    fun changePassword(oldPass:String,newPass:String): LiveData<ResourceWrapper<BaseResponse?>> {
        return object : LoadData<BaseResponse,BaseResponse>() {
            override fun processResponse(apiResponse: ApiResponse<BaseResponse>): BaseResponse? {
                return handlerResponse(apiResponse)
            }

            override fun getCallService(): LiveData<ApiResponse<BaseResponse>> {
                return userService.changePass(token,oldPass,newPass)
            }

        }.getLiveData()
    }
}