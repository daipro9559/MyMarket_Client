package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.entity.User
import com.google.gson.annotations.Expose

data class RegisterResponse(@Expose
                            val success: Boolean,
                            @Expose
                            val data: User,
                            @Expose
                            val message: String)