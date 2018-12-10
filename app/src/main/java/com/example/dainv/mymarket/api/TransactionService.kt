package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.api.response.TransactionResponse
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.Notification
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.*

interface TransactionService {

    @POST("transactions")
    fun confirmTransaction(@Header(Constant.HEADER) token:String?,@Body notification:Notification):LiveData<ApiResponse<BaseResponse>>

    @GET("transactions")
    fun getTransaction(@Header(Constant.HEADER) token: String?,@QueryMap map: Map<String,String>) : LiveData<ApiResponse<TransactionResponse>>
}