package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.ErrorResponse
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.api.ItemService
import com.example.dainv.mymarket.api.response.AddItemResponse
import com.example.dainv.mymarket.api.response.CategoryResponse
import com.example.dainv.mymarket.api.response.ItemResponse
import com.example.dainv.mymarket.model.AddItemBody
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class ItemRepository
@Inject constructor(
        val itemService: ItemService,
        val sharePreferencHelper: SharePreferencHelper
) : BaseRepository() {
    fun getAllCategory() = object : LoadData<List<Category>, CategoryResponse>() {

        override fun processResponse(apiResponse: ApiResponse<CategoryResponse>): List<Category>? {
            if (apiResponse.throwable != null) {
                Timber.e("fail call api")
            }
            if (apiResponse.code == 401){
                errorLiveData.value = ErrorResponse.UN_AUTHORIZED
            }
            return apiResponse.body?.data
        }

        override fun loadFromDB(): LiveData<List<Category>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        override fun getCallService() = itemService.getCategories(sharePreferencHelper.getString(Constant.TOKEN, null)!!)
    }.resultData

    fun getItems(queryMap: Map<String, String>) = object : LoadData<List<Item>, ItemResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ItemResponse>): List<Item>? {
            if (apiResponse.code == 401){
                errorLiveData.value = ErrorResponse.UN_AUTHORIZED
            }
            return apiResponse.body?.data
        }

        override fun loadFromDB(): LiveData<List<Item>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCallService(): LiveData<ApiResponse<ItemResponse>> {
            return itemService.getItems(sharePreferencHelper.getString(Constant.TOKEN, null)!!,queryMap)
        }

    }.resultData

    fun sellItem(multipartBody: MultipartBody) = object : LoadData<AddItemResponse,AddItemResponse>(){
        override fun processResponse(apiResponse: ApiResponse<AddItemResponse>): AddItemResponse? {
            if (apiResponse.code == 401){
                errorLiveData.value = ErrorResponse.UN_AUTHORIZED
            }
            return apiResponse.body
        }

        override fun loadFromDB(): LiveData<AddItemResponse> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCallService(): LiveData<ApiResponse<AddItemResponse>> {
         return itemService.sellItem(sharePreferencHelper.getString(Constant.TOKEN,null)!!,multipartBody)
        }

    }.resultData
}