package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.NotificationService
import com.example.dainv.mymarket.api.response.NotificationResponse
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class NotificationRepository
@Inject
constructor(sharePreferencHelper: SharePreferencHelper,
            private val notificationService: NotificationService)
    :BaseRepository(sharePreferencHelper){

    fun getAllNotification(page:Int) = object : LoadData<NotificationResponse,NotificationResponse>(){
        override fun processResponse(apiResponse: ApiResponse<NotificationResponse>): NotificationResponse? {
           return handlerCallApi(apiResponse)
        }

        override fun getCallService() = notificationService.getNotification(token,page)

    }.getLiveData()
}