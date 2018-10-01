package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class RegisterResponse(@Expose
                            val success: Boolean,
                            @Expose
                            val user: User,
                            @Expose
                            val message: String)