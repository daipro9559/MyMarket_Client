package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.StandService
import com.example.dainv.mymarket.api.response.AddItemResponse
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.CommentsResponse
import com.example.dainv.mymarket.api.response.ListStandResponse
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import okhttp3.MultipartBody
import javax.inject.Inject

class StandRepository @Inject constructor(sharePreferencHelper: SharePreferencHelper,
                                          private val standService: StandService)
    : BaseRepository(sharePreferencHelper) {

    fun createStand(multipartBody: MultipartBody) = object : LoadData<Boolean, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            return apiResponse?.body?.success
        }

        override fun getCallService() = standService.createStand(token, multipartBody)
    }.getLiveData()

    fun addItemToStand(multipartBody: MultipartBody) = object : LoadData<AddItemResponse, AddItemResponse>() {
        override fun processResponse(apiResponse: ApiResponse<AddItemResponse>): AddItemResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService(): LiveData<ApiResponse<AddItemResponse>> {
            return standService.addItemToStand(token, multipartBody)
        }

    }.getLiveData()

    fun getMyStands() = object : LoadData<ListStandResponse, ListStandResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ListStandResponse>): ListStandResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService() = standService.getMyStands(token)

    }.getLiveData()

    fun getStands() = object : LoadData<ListStandResponse, ListStandResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ListStandResponse>): ListStandResponse? {
            return handlerResponse(apiResponse)
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

    fun addItemFromTransaction(standID: String, itemID: String) = object : LoadData<BaseResponse, BaseResponse>() {

        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): BaseResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService() = standService.addItemToStandByTransaction(token, standID, itemID)

    }.getLiveData()

    fun createComment(comment: String, standID: String) = object : LoadData<BaseResponse, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): BaseResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService(): LiveData<ApiResponse<BaseResponse>> {
            return standService.createComment(token,standID,comment)
        }

    }.getLiveData()

    fun getComments( standID: String,page:Int) = object : LoadData<CommentsResponse, CommentsResponse>() {
        override fun processResponse(apiResponse: ApiResponse<CommentsResponse>): CommentsResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService(): LiveData<ApiResponse<CommentsResponse>> {
            return standService.getCommentOfStand(token,standID,page)
        }

    }.getLiveData()
}