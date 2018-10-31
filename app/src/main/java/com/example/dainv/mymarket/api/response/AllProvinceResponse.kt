package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.Province
import com.google.gson.annotations.Expose

data class AllProvinceResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String,
        @Expose
        val data:List<Province>
)