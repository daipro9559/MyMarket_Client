package com.example.dainv.mymarket.service.response

import com.example.dainv.mymarket.model.User
import com.google.gson.annotations.Expose

data class RegisterResponse(@Expose
                            val success: Boolean,
                            @Expose
                            val data: User,
                            @Expose
                            val message: String)