package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.District
import com.example.dainv.mymarket.model.Province
import com.example.dainv.mymarket.api.AddressService
import com.example.dainv.mymarket.api.response.DistrictResponse
import com.example.dainv.mymarket.api.response.AllProvinceResponse
import com.example.dainv.mymarket.api.response.BaseResponse
import com.example.dainv.mymarket.base.BaseRepository
import com.example.dainv.mymarket.database.AppDatabase
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class AddressRepository
@Inject constructor(
        val addressService: AddressService,
        sharePreferencHelper: SharePreferencHelper,
        val appDatabase: AppDatabase
):BaseRepository(sharePreferencHelper){

     fun getAllProvince() =  object: LoadData<List<Province>,AllProvinceResponse>(){
        override fun processResponse(apiResponseAll: ApiResponse<AllProvinceResponse>): List<Province>? {
            return apiResponseAll.body?.data
        }

        override fun getCallService(): LiveData<ApiResponse<AllProvinceResponse>> {
            return addressService.getAllProvince(token!!)
        }

         override fun isLoadFromDb(): Boolean {
             return true
         }

         override fun needFetchData(resultType: List<Province>?): Boolean {
             return resultType == null || resultType.isEmpty()
         }
         override fun loadFromDB(): LiveData<List<Province>> {
             return appDatabase.provinceDao().getAll()
         }

         override fun saveToDatabase(value: List<Province>?) {
            appDatabase.provinceDao().saveAll(value!!)
         }

    }.getLiveData()

     fun getAllDistrict(provinceID:Int) = object :LoadData<List<District>,DistrictResponse>(){
        override fun processResponse(apiResponse: ApiResponse<DistrictResponse>): List<District>? {
            return apiResponse.body?.data
        }

        override fun getCallService(): LiveData<ApiResponse<DistrictResponse>> {
           return addressService.getAllDistrict(token!!,
                   provinceID)
        }

         override fun loadFromDB(): LiveData<List<District>> {
             return appDatabase.districtDao().getAllDistrict(provinceID)
         }

         override fun needFetchData(resultType: List<District>?): Boolean {
             return resultType == null || resultType.isEmpty()
         }

         override fun isLoadFromDb() = true

         override fun saveToDatabase(value: List<District>?) {
             appDatabase.districtDao().saveAll(value!!)
         }

    }.getLiveData()

}