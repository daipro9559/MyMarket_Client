package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.NotificationResponse
import com.example.dainv.mymarket.api.response.NotificationSettingResponse
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.*

interface NotificationService {
    @GET("notifications")
    fun getNotification(@Header(Constant.HEADER) token: String?,@Query("page") page:Int): LiveData<ApiResponse<NotificationResponse>>

    @DELETE("notifications/{notificationID}")
    fun delete(@Header(Constant.HEADER) token :String ?,@Path("notificationID") notificationID:String): LiveData<ApiResponse<BaseResponse>>

    @POST("conditionNotify/{conditionID}")
    @FormUrlEncoded
    fun saveSetting(@Header(Constant.HEADER) token:String?,
                    @Path("conditionID")conditionID:String,
                    @Field("isEnable") isEnable:Boolean?,
                    @Field("categoryID") categoryID:Int?,
                    @Field("provinceID") provinceID:Int?,
                    @Field("districtID") districtID:Int?
                    ): LiveData<ApiResponse<BaseResponse>>

    @GET("conditionNotify")
    fun getSetting(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<NotificationSettingResponse>>

    @POST("notifications/requestTransaction")
    @FormUrlEncoded
    fun requestTransaction(@Header(Constant.HEADER) token: String?, @Field("itemID") itemID: String,
                           @Field("sellerID") sellerId:String,
                           @Field("itemName") itemName:String,
                           @Field("price") price:Long)
            : LiveData<ApiResponse<BaseResponse>>


}