package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.api.response.CategoryResponse
import com.example.dainv.mymarket.api.response.ItemResponse
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface ItemService {
    @GET("categories")
    fun getCategories(@Header(Constant.HEADER) token: String): LiveData<ApiResponse<CategoryResponse>>

    @GET("items")
    fun getItems(@Header(Constant.HEADER) token: String,@QueryMap queries: Map<String,String>):LiveData<ApiResponse<ItemResponse>>
}