package com.example.dainv.mymarket.service

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.model.LoginResponse
import com.example.dainv.mymarket.model.RegisterResponse
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("email") email: String, @Field("password") password: String): LiveData<ApiResponse<LoginResponse>>

    @POST("user/register")
    @FormUrlEncoded
    fun register(@Field("email") email: String,
                 @Field("password") password: String,
                 @Field("phone") phone: String,
                 @Field("name") name: String):LiveData<ApiResponse<RegisterResponse>>
}