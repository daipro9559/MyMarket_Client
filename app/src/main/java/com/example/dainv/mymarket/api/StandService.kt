package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.AddItemResponse
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.ListStandResponse
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.util.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StandService {
    @POST("stands")
    fun createStand(@Header(Constant.HEADER) token: String?, @Body multipartBody: MultipartBody): LiveData<ApiResponse<BaseResponse>>

    @POST("stands/items")
    fun addItemToStand(@Header(Constant.HEADER) token: String?, @Body multipartBody: MultipartBody): LiveData<ApiResponse<AddItemResponse>>

    @GET("stands/myStands")
    fun getMyStands(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<ListStandResponse>>

    @GET("stands")
    fun getStands(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<ListStandResponse>>

    @PUT("stands")
    fun upadate(@Header(Constant.HEADER) token: String?, @Body multipartBody: MultipartBody):LiveData<ApiResponse<BaseResponse>>

    @DELETE("stands/{standID}")
    fun deleteStand(@Header(Constant.HEADER) token: String?,@Path("standID") standID: String):LiveData<ApiResponse<BaseResponse>>

    @POST("stands/follow")
    @FormUrlEncoded
    fun followStand(@Header(Constant.HEADER) token: String?,@Field("standID")standID: String) :LiveData<ApiResponse<BaseResponse>>

    @DELETE("stands/follow/{standID}")
    fun unFollow(@Header(Constant.HEADER) token: String?, @Path("standID")standID: String) :LiveData<ApiResponse<BaseResponse>>

    @PUT("stands/{standID}/items")
    @FormUrlEncoded
    fun addItemToStandByTransaction(@Header(Constant.HEADER) token: String?, @Path("standID")standID: String,
                                    @Field("itemID") itemID:String):LiveData<ApiResponse<BaseResponse>>
}