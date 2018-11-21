package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.Item
import com.google.gson.annotations.Expose

class ItemDetailResponse (
    @Expose
    val success:Boolean,
    @Expose
    val message:String,
    @Expose
    val data:Item
)