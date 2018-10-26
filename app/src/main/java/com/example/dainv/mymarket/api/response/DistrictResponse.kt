package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.District
import com.google.gson.annotations.Expose

data class DistrictResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String,
        @Expose
        val data:List<District>
)