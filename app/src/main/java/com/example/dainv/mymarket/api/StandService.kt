package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.POST

interface StandService {
    @POST("stands")
    fun createStand() : LiveData<ApiResponse<BaseResponse>>
}