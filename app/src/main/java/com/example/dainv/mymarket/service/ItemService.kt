package com.example.dainv.mymarket.service

import com.example.dainv.mymarket.base.Constant
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header

interface ItemService {
    @GET("categories")
    fun getCategories(@Header(Constant.HEADER) token: String)
}