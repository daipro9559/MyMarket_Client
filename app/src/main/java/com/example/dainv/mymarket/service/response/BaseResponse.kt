package com.example.dainv.mymarket.service.response

import com.google.gson.annotations.Expose

abstract class BaseResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String
)
