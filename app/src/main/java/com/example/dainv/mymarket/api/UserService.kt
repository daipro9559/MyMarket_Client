package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.LoginResponse
import com.example.dainv.mymarket.api.response.PhoneResponse
import com.example.dainv.mymarket.api.response.ProfileResponse
import com.example.dainv.mymarket.api.response.RegisterResponse
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.*

interface UserService {
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("email") email: String
              , @Field("password") password: String
              , @Field("tokenFireBase") tokenFireBase: String): LiveData<ApiResponse<LoginResponse>>

    @POST("user/register")
    @FormUrlEncoded
    fun register(@Field("email") email: String,
                 @Field("password") password: String,
                 @Field("phone") phone: String,
                 @Field("name") name: String): LiveData<ApiResponse<RegisterResponse>>

    @GET("user/phone")
    fun getPhoneNumber(@Header(Constant.HEADER) token: String?, @Query("sellerID") sellerID: String): LiveData<ApiResponse<PhoneResponse>>

    @GET("user/profile")
    fun getProfile(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<ProfileResponse>>

    @POST("user/updateToSeller")
    fun updateToSeller(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<BaseResponse>>

    @POST("user/logout")
    fun logout(@Header(Constant.HEADER) token: String?):LiveData<ApiResponse<BaseResponse>>

}