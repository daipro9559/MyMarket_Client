package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.*
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.api.response.LoginResponse
import com.example.dainv.mymarket.ui.addAddress.AddAdressViewModel
import com.example.dainv.mymarket.util.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("email") email: String
              , @Field("password") password: String
              , @Field("tokenFirebase") tokenFirebase: String): LiveData<ApiResponse<LoginResponse>>

    @POST("user/register")
    @FormUrlEncoded
    fun register(@Field("email") email: String,
                 @Field("password") password: String,
                 @Field("phone") phone: String,
                 @Field("name") name: String): LiveData<ApiResponse<RegisterResponse>>

    @GET("user/phone")
    fun getPhoneNumber(@Header(Constant.HEADER) token: String?, @Query("sellerID") sellerID: String): LiveData<ApiResponse<PhoneResponse>>

    @GET("user/profile")
    fun getMyProfile(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<ProfileResponse>>

    @GET("user/profile/{userID}")
    fun getOtherProfile(@Header(Constant.HEADER) token: String?, @Path("userID") userID: String): LiveData<ApiResponse<ProfileResponse>>

    @POST("user/updateToSeller")
    fun updateToSeller(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<BaseResponse>>

    @POST("user/logout")
    fun logout(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<BaseResponse>>

    @POST("user/changePass")
    @FormUrlEncoded
    fun changePass(@Header(Constant.HEADER) token: String?, @Field("oldPassword") oldPass: String,
                   @Field("newPassword") newPass: String): LiveData<ApiResponse<BaseResponse>>

    @POST("user")
    fun updateProfile(@Header(Constant.HEADER) token: String?, @Body multipartBody: MultipartBody): LiveData<ApiResponse<BaseResponse>>

    // admin
    @GET("admin/users")
    fun getUsers(@Header(Constant.HEADER) token: String?, @Query("page") page: Int): LiveData<ApiResponse<ListUserResponse>>

    @POST("user/addAddress")
    fun addAddress(@Header(Constant.HEADER) token: String?, @Body addAddressParam: AddAdressViewModel.AddAddressParam): LiveData<ApiResponse<BaseResponse>>

    @POST("admin/logout")
    fun adminLogout(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<BaseResponse>>

    @POST("user/forgot")
    @FormUrlEncoded
    fun forgot(@Field("email") email: String): LiveData<ApiResponse<BaseResponse>>

    @POST("user/changePassByCode")
    @FormUrlEncoded
    fun changePassByCode(@Field("code") code: Long, @Field("email") email: String): LiveData<ApiResponse<BaseResponse>>
}