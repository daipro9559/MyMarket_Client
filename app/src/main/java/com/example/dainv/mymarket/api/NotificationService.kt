package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.NotificationResponse
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.*

interface NotificationService {
    @GET("notifications")
    fun getNotification(@Header(Constant.HEADER) token: String?,@Query("page") page:Int): LiveData<ApiResponse<NotificationResponse>>

    @DELETE("notifications/{notificationID}")
    fun delete(@Header(Constant.HEADER) token :String ?,@Path("notificationID") notificationID:String): LiveData<ApiResponse<BaseResponse>>
}