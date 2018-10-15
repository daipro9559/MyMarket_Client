package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.District
import com.example.dainv.mymarket.model.Province
import com.example.dainv.mymarket.service.AddressService
import com.example.dainv.mymarket.service.ItemService
import com.example.dainv.mymarket.service.response.DistrictResponse
import com.example.dainv.mymarket.service.response.ProvinceResponse
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class AddressRepository
@Inject constructor(
        val addressService: AddressService,
        val sharePreferencHelper: SharePreferencHelper
){

    public fun getAllProvince() =  object: LoadData<List<Province>,ProvinceResponse>(){
        override fun processResponse(apiResponse: ApiResponse<ProvinceResponse>): List<Province>? {
            return apiResponse.body?.data
        }

        override fun loadFromDB(): LiveData<List<Province>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCallService(): LiveData<ApiResponse<ProvinceResponse>> {
            return addressService.getAllProvince(sharePreferencHelper.getString(Constant.TOKEN,null)!!)
        }

    }
    public fun getAllDistrict(provinceID:Int) = object :LoadData<List<District>,DistrictResponse>(){
        override fun processResponse(apiResponse: ApiResponse<DistrictResponse>): List<District>? {
            return apiResponse.body?.data
        }

        override fun loadFromDB(): LiveData<List<District>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isLoadFromDb(isForce: Boolean): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCallService(): LiveData<ApiResponse<DistrictResponse>> {
           return addressService.getAllDistrict(sharePreferencHelper.getString(Constant.TOKEN,null)!!,
                   provinceID)
        }

    }
}