package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.NotificationResponse
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NotificationService {
    @GET("notifications")
    fun getNotification(@Header(Constant.HEADER) token: String?,@Query("page") page:Int): LiveData<ApiResponse<NotificationResponse>>
}