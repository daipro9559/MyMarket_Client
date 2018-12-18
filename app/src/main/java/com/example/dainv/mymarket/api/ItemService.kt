package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.*
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.AddItemBody
import com.example.dainv.mymarket.util.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ItemService {
    @GET("categories")
    fun getCategories(@Header(Constant.HEADER) token: String?): LiveData<ApiResponse<CategoryResponse>>

    @GET("items")
    fun getItems(@Header(Constant.HEADER) token: String?, @QueryMap queries: Map<String, String>): LiveData<ApiResponse<ItemResponse>>

    @GET("items/{itemID}")
    fun getItemDetail(@Header(Constant.HEADER) token: String?, @Path("itemID") itemID: String): LiveData<ApiResponse<ItemDetailResponse>>

    @POST("items")
    fun postItem(@Header(Constant.HEADER) token: String?, @Body multipartBody: MultipartBody): LiveData<ApiResponse<AddItemResponse>>

    @GET("items/mark")
    fun getItemsMarked(@Header(Constant.HEADER) token: String?, @Query("page") page: Int): LiveData<ApiResponse<ItemResponse>>

    @POST("items/mark")
    @FormUrlEncoded
    fun markItem(@Header(Constant.HEADER) token: String?, @Field("itemID") itemID: String): LiveData<ApiResponse<BaseResponse>>

    @DELETE("items/mark/{itemID}")
    fun unMarkItem(@Header(Constant.HEADER) token: String?, @Path("itemID") itemID: String): LiveData<ApiResponse<BaseResponse>>

    @DELETE("items/{itemID}")
    fun deleteItem(@Header(Constant.HEADER) token: String?, @Path("itemID") itemID: String): LiveData<ApiResponse<BaseResponse>>


}