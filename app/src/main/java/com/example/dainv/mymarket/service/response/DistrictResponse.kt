package com.example.dainv.mymarket.service.response

import com.example.dainv.mymarket.model.District
import com.example.dainv.mymarket.model.Province
import com.google.gson.annotations.Expose

data class DistrictResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String,
        @Expose
        val data:List<District>
)