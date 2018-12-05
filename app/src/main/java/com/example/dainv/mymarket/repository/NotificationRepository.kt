package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.NotificationService
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.NotificationResponse
import com.example.dainv.mymarket.api.response.NotificationSettingResponse
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.model.NotificationSetting
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

    fun deleteNotification(id: String) = object : LoadData<BaseResponse,BaseResponse>(){
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): BaseResponse? {
            return handlerCallApi(apiResponse)
        }

        override fun getCallService() = notificationService.delete(token,id)

    }.getLiveData()
    fun getSetting() = object : LoadData<NotificationSettingResponse,NotificationSettingResponse>(){
        override fun processResponse(apiResponse: ApiResponse<NotificationSettingResponse>): NotificationSettingResponse? {
            return handlerCallApi(apiResponse)
        }

        override fun getCallService()= notificationService.getSetting(token)


    }.getLiveData()
    fun saveSetting(notificationSetting: NotificationSetting) = object : LoadData<BaseResponse,BaseResponse>(){
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): BaseResponse? {
            return handlerCallApi(apiResponse)
        }

        override fun getCallService()= notificationService.saveSetting(token,
                notificationSetting.conditionID!!,
                notificationSetting.isEnable,
                notificationSetting.Category!!.categoryID,
                notificationSetting.Province!!.provinceID,
                notificationSetting.District!!.districtID)


    }.getLiveData()

    fun requestBuy(itemID:String,sellerID:String,itemName:String,price:Long) = object :LoadData<BaseResponse,BaseResponse>(){
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): BaseResponse? {
            return handlerCallApi(apiResponse)
        }

        override fun getCallService() = notificationService.requestBuyItem(token,itemID,sellerID,itemName,price)

    }.getLiveData()
}