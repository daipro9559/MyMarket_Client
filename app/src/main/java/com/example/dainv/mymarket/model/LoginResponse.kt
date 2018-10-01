package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

public class LoginResponse(
        @Expose
        val success: Boolean,
        @Expose
        val user: User,
        @Expose
        val message: String,
        @Expose
        val token: String)