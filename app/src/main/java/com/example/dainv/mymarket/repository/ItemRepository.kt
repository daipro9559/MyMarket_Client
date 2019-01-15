package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.entity.Category
import com.example.dainv.mymarket.entity.ErrorResponse
import com.example.dainv.mymarket.entity.Item
import com.example.dainv.mymarket.api.ItemService
import com.example.dainv.mymarket.api.response.*
import com.example.dainv.mymarket.database.AppDatabase
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class ItemRepository
@Inject constructor(
        val appDatabase: AppDatabase,
        val itemService: ItemService,
        sharePreferencHelper: SharePreferencHelper
) : BaseRepository(sharePreferencHelper) {
    fun getAllCategory() = object : LoadData<List<Category>, CategoryResponse>() {
        override fun processResponse(apiResponse: ApiResponse<CategoryResponse>): List<Category>? {
            if (apiResponse.throwable != null) {
                Timber.e("fail call api")
            }
            if (apiResponse.code == 401) {
                errorLiveData.value = ErrorResponse.UN_AUTHORIZED
            }
            return apiResponse.body?.data
        }

        override fun needFetchData(resultType: List<Category>?): Boolean {
            return resultType == null || resultType.isEmpty()
        }

        override fun loadFromDB(): LiveData<List<Category>> {
            return appDatabase.categoryDao().getAll()
        }

        override fun saveToDatabase(value: List<Category>?) {
            appDatabase.categoryDao().saveAll(value!!)
        }

        override fun isLoadFromDb(): Boolean {
            return true
        }

        override fun getCallService() = itemService.getCategories(sharePreferencHelper.getString(Constant.TOKEN, null)!!)
    }.getLiveData()

    fun getItems(queryMap: Map<String, String>) = object : LoadData<ItemResponse, ItemResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ItemResponse>): ItemResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService(): LiveData<ApiResponse<ItemResponse>> {
            return itemService.getItems(token, queryMap)
        }

    }.getLiveData()

    fun sellItem(multipartBody: MultipartBody) = object : LoadData<AddItemResponse, AddItemResponse>() {
        override fun processResponse(apiResponse: ApiResponse<AddItemResponse>): AddItemResponse? {
            if (apiResponse.code == 401) {
                errorLiveData.value = ErrorResponse.UN_AUTHORIZED
            }
            return apiResponse.body
        }

        override fun getCallService(): LiveData<ApiResponse<AddItemResponse>> {
            return itemService.postItem(token, multipartBody)
        }

    }.getLiveData()

    fun getAllItemMarked(page: Int) = object : LoadData<ItemResponse, ItemResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ItemResponse>): ItemResponse? {
            if (apiResponse.code == 401) {
                errorLiveData.value = ErrorResponse.UN_AUTHORIZED
            }
            return apiResponse.body
        }

        override fun getCallService() = itemService.getItemsMarked(token!!, page)
    }.getLiveData()

    fun markItem(itemID: String) = object : LoadData<Boolean, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            return apiResponse?.body?.success
        }

        override fun getCallService() = itemService.markItem(token!!, itemID)
    }.getLiveData()

    fun unMarkItem(itemID: String) = object : LoadData<Boolean, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            return apiResponse?.body?.success
        }

        override fun getCallService(): LiveData<ApiResponse<BaseResponse>> {
            return itemService.unMarkItem(token!!, itemID)
        }

    }.getLiveData()

    fun delete(itemID: String) = object : LoadData<Boolean, BaseResponse>() {
        override fun processResponse(apiResponse: ApiResponse<BaseResponse>): Boolean? {
            if (apiResponse?.body != null) {
                return apiResponse.body?.success
            }
            return null
        }

        override fun getCallService() = itemService.deleteItem(token, itemID)

    }.getLiveData()

    fun getItemDetail(itemID: String) = object : LoadData<Item, ItemDetailResponse>() {
        override fun processResponse(apiResponse: ApiResponse<ItemDetailResponse>): Item? {
            if (apiResponse?.body != null) {
                return apiResponse.body?.data
            }
            return null
        }

        override fun getCallService() = itemService.getItemDetail(token, itemID)

    }.getLiveData()

    fun getCategory(id: Int) = appDatabase.categoryDao().getCategory(id)
    // userID is id of user has item_view_pager

    fun findOnMap(queryMap: Map<String, String?>)  = object : LoadData<ListItemOnMap,ListItemOnMap>(){
        override fun processResponse(apiResponse: ApiResponse<ListItemOnMap>): ListItemOnMap? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService(): LiveData<ApiResponse<ListItemOnMap>> {
            return itemService.findItemOnMap(token,queryMap)
        }

    }.getLiveData()

    fun updateItem(itemID: String,multipartBody: MultipartBody)  = object:LoadData<AddItemResponse,AddItemResponse>(){
        override fun processResponse(apiResponse: ApiResponse<AddItemResponse>): AddItemResponse? {
            return handlerResponse(apiResponse)
        }

        override fun getCallService(): LiveData<ApiResponse<AddItemResponse>> {
            return itemService.updateItem(token,itemID,multipartBody)
        }

    }.getLiveData()
}