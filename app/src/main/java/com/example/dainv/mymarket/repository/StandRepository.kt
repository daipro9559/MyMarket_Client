package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.StandService
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.ListStandResponse
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.model.Stand
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import okhttp3.MultipartBody
import javax.inject.Inject

class StandRepository @Inject constructor(sharePreferencHelper: SharePreferencHelper,
                                          private val standService: StandService)
    :BaseRepository(sharePreferencHelper) {

    fun createStand(multipartBody: MultipartBody) = object :LoadData<Boolean,BaseResponse>(){
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            return apiResponse?.body?.success
        }

        override fun getCallService() = standService.createStand(token,multipartBody)
    }.getLiveData()

    fun getMyStands() = object : LoadData<List<Stand>,ListStandResponse>(){
        override fun processResponse(apiResponse: ApiResponse<ListStandResponse>): List<Stand>? {
           return  apiResponse?.body?.data
        }

        override fun getCallService() = standService.getMyStands(token)

    }.getLiveData()

    fun getStands() = object: LoadData<List<Stand>,ListStandResponse>(){
        override fun processResponse(apiResponse: ApiResponse<ListStandResponse>): List<Stand>? {
            return  apiResponse?.body?.data
        }

        override fun getCallService() = standService.getStands(token)

    }.getLiveData()

    fun follow(standID: String) = object : LoadData<Boolean, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            return apiResponse?.body?.success
        }

        override fun getCallService() = standService.followStand(token!!, standID)
    }.getLiveData()

    fun unFollow(standID: String) = object : LoadData<Boolean, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            return apiResponse?.body?.success
        }

        override fun getCallService(): LiveData<ApiResponse<BaseResponse>> {
            return standService.unFollow(token!!, standID)
        }

    }.getLiveData()
    fun delete(standID: String) = object : LoadData<Boolean, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            return apiResponse?.body?.success
        }

        override fun getCallService(): LiveData<ApiResponse<BaseResponse>> {
            return standService.deleteStand(token!!, standID)
        }

    }.getLiveData()

}