package com.example.dainv.mymarket.api.response

import com.google.gson.annotations.Expose

data class BaseResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String
)
