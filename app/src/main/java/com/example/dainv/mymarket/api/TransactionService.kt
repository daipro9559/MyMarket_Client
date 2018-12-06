package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.Notification
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TransactionService {

    @POST("transactions")
    fun confirmTransaction(@Header(Constant.HEADER) token:String?,@Body notification:Notification):LiveData<ApiResponse<BaseResponse>>
}