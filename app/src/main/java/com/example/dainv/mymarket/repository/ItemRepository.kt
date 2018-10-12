package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.ResourceWrapper
import com.example.dainv.mymarket.service.ItemService
import com.example.dainv.mymarket.service.response.CategoryResponse
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import timber.log.Timber
import javax.inject.Inject

class ItemRepository
    @Inject constructor(
            val itemService: ItemService,
            val sharePreferencHelper: SharePreferencHelper
    ): BaseRepository(){
    public fun getAllCategory()=object : LoadData<List<Category>,CategoryResponse>(){

        override fun processResponse(apiResponse: ApiResponse<CategoryResponse>): List<Category>? {
            if (apiResponse.throwable!=null){
                Timber.e("fail call api")
            }
            return apiResponse.body?.data
        }

        override fun loadFromDB(): LiveData<List<Category>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCallService() = itemService.getCategories(sharePreferencHelper.getString(Constant.TOKEN,null)!!)


    }.resultData
}