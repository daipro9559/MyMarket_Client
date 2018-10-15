package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class Province(
        @Expose
        val provinceID:Int,
        @Expose
        val name:String
)