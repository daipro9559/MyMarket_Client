package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.User
import com.google.gson.annotations.Expose

data class ProfileResponse(
        @Expose
        val success: Boolean,
        @Expose
        val message: String,
        @Expose
        val data: User
)