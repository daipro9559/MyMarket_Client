package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.entity.ItemMap
import com.google.gson.annotations.Expose

class ListItemOnMap (
        @Expose
        val success: Boolean,
        @Expose
        val message: String,
        @Expose
        val lastPage: Boolean,
        @Expose
        val data: List<ItemMap>
)