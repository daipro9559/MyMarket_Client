package com.example.dainv.mymarket.api

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.api.response.DistrictResponse
import com.example.dainv.mymarket.api.response.ProvinceResponse
import com.example.dainv.mymarket.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AddressService {

    @GET("province")
    fun getAllProvince(@Header(Constant.HEADER) token: String): LiveData<ApiResponse<ProvinceResponse>>

    @GET("provinces/{provinceID}/districts")
    fun getAllDistrict(@Header(Constant.HEADER) token: String, @Path("provinceID") provinceID: Int): LiveData<ApiResponse<DistrictResponse>>
}