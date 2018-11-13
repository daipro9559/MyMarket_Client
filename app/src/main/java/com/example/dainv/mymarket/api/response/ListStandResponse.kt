package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.Stand
import com.google.gson.annotations.Expose

class ListStandResponse(
        @Expose
        val success:Boolean,
        @Expose
        val message:String,
        @Expose
        val data:List<Stand>
)