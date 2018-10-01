package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class User(
        @Expose
        val id: Int,
        @Expose
        val name: String,
        @Expose
        val email:String,
        @Expose
        val phone : String,
        @Expose
        val createdAt:String,
        @Expose
        val updatedAt:String)