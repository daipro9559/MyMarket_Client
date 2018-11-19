package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.Item
import com.google.gson.annotations.Expose


data class AddItemResponse(
        @Expose
        val success: Boolean,
        @Expose
        val message: String,
        @Expose
        val data: AddItemData
) {
    data class AddItemData(
            @Expose
            val itemID: String
    )
}
