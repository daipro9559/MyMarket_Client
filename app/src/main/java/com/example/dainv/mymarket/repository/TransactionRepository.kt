package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.TransactionService
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.database.AppDatabase
import com.example.dainv.mymarket.model.Notification
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
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
}