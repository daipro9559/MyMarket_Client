package com.example.dainv.mymarket.ui.additem

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.AddItemResponse
import com.example.dainv.mymarket.model.*
import com.example.dainv.mymarket.repository.AddressRepository
import com.example.dainv.mymarket.repository.ItemRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class AddItemCase @Inject constructor(val addressRepository: AddressRepository,
                                      val itemRepository: ItemRepository){

    public fun getAllCategory():LiveData <ResourceWrapper<List<Category>?>>{
        return itemRepository.getAllCategory()
    }
    public fun getAllProvince() :LiveData<ResourceWrapper<List<Province>?>>{
        return addressRepository.getAllProvince()
    }
    public fun getDistrics(provinceID:Int): LiveData<ResourceWrapper<List<District>?>>{
        return addressRepository.getAllDistrict(provinceID)
    }
    public fun sellItem(multipartBody: MultipartBody?,addItemBody: AddItemBody):LiveData<ResourceWrapper<AddItemResponse?>>{
        return itemRepository.sellItem(multipartBody!!)
    }
}