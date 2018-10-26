package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.Province
import com.google.gson.annotations.Expose

data class ProvinceResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String,
        @Expose
        val data:List<Province>
)