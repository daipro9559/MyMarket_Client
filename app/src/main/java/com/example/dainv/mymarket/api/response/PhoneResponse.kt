package com.example.dainv.mymarket.api.response

import com.google.gson.annotations.Expose

data class PhoneResponse(
        @Expose
        val success: Boolean,
        @Expose
        val message: String,
        @Expose
        val data: PhoneData
)

data class PhoneData (
    @Expose
    val phone: String
)