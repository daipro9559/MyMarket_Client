package com.example.dainv.mymarket.service

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.service.response.CategoryResponse
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header

interface ItemService {
    @GET("categories")
    fun getCategories(@Header(Constant.HEADER) token: String):LiveData<ApiResponse<CategoryResponse>>
}