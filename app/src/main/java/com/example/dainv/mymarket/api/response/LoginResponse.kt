package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.model.User
import com.google.gson.annotations.Expose

data class LoginResponse(
        @Expose
        val success: Boolean,
        @Expose
        val data: Data,
        @Expose
        val message: String
)

class Data(
        @Expose
        val user: User,
        @Expose
        val token: String
)