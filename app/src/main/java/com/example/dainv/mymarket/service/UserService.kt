package com.example.dainv.mymarket.service

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.LoginResponse
import com.example.dainv.mymarket.service.response.PhoneResponse
import com.example.dainv.mymarket.service.response.RegisterResponse
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.*

interface UserService {
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("email") email: String, @Field("password") password: String): LiveData<ApiResponse<LoginResponse>>

    @POST("user/register")
    @FormUrlEncoded
    fun register(@Field("email") email: String,
                 @Field("password") password: String,
                 @Field("phone") phone: String,
                 @Field("name") name: String): LiveData<ApiResponse<RegisterResponse>>

    @GET("user/phone")
    fun getPhoneNumber(@Header(Constant.HEADER) token:String?, @Query("sellerID") sellerID: Int): LiveData<ApiResponse<PhoneResponse>>
}