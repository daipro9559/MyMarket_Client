package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class District(
        @Expose
        val districtID:Int,
        @Expose
        val districtName:String,
        @Expose
        val provinceID:Int
)