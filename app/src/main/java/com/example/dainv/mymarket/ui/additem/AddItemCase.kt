package com.example.dainv.mymarket.ui.additem

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.api.response.AddItemResponse
import com.example.dainv.mymarket.entity.*
import com.example.dainv.mymarket.repository.AddressRepository
import com.example.dainv.mymarket.repository.ItemRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class AddItemCase @Inject constructor(private val addressRepository: AddressRepository,
                                      private val itemRepository: ItemRepository){

     fun getAllCategory():LiveData <ResourceWrapper<List<Category>?>>{
        return itemRepository.getAllCategory()
    }
     fun getAllProvince() :LiveData<ResourceWrapper<List<Province>?>>{
        return addressRepository.getAllProvince()
    }
     fun getDistricts(provinceID:Int): LiveData<ResourceWrapper<List<District>?>>{
        return addressRepository.getAllDistrict(provinceID)
    }
     fun sellItem(multipartBody: MultipartBody?,addItemBody: AddItemBody):LiveData<ResourceWrapper<AddItemResponse?>>{
        return itemRepository.sellItem(multipartBody!!)
    }
}