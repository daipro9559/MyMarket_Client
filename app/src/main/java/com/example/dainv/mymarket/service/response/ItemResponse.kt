package com.example.dainv.mymarket.service.response

import com.example.dainv.mymarket.model.District
import com.example.dainv.mymarket.model.Item
import com.google.gson.annotations.Expose

data class ItemResponse(
    @Expose
    val success:Boolean,
    @Expose
    val message:String,
    @Expose
    val data:List<Item>
)
