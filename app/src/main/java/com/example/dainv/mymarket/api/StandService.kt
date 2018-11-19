package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.ListStandResponse
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.util.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface StandService {
    @POST("stands")
    fun createStand(@Header(Constant.HEADER) token: String?, @Body multipartBody: MultipartBody): LiveData<ApiResponse<BaseResponse>>

    @GET("stands/myStands")
    fun getMyStands(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<ListStandResponse>>

    @GET("stands")
    fun getStands(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<ListStandResponse>>

    @PUT("stands")
    fun upadate(@Header(Constant.HEADER) token: String?, @Body multipartBody: MultipartBody):LiveData<ApiResponse<BaseResponse>>
}