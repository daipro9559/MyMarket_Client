package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.TransactionService
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.TransactionResponse
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.database.AppDatabase
import com.example.dainv.mymarket.model.Notification
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import retrofit2.http.QueryMap
import javax.inject.Inject

class TransactionRepository
    @Inject
    constructor(val appDatabase: AppDatabase,
                val transactionService: TransactionService,
                sharePreferencHelper: SharePreferencHelper)
    :BaseRepository(sharePreferencHelper){

    fun confirmTransaction(notification:Notification) = object :LoadData<BaseResponse,BaseResponse>(){
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): BaseResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService() = transactionService.confirmTransaction(token,notification)

    }.getLiveData()

    fun getTransaction(map:Map<String,String>) = object :LoadData<TransactionResponse,TransactionResponse>(){
        override fun processResponse(apiResponse: ApiResponse<TransactionResponse>): TransactionResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService() = transactionService.getTransaction(token,map)

    }.getLiveData()
}