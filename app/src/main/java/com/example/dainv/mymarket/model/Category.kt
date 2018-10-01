package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class Category(
        @Expose
        val id:Int,
        @Expose
        val name:String,
        @Expose
        val imageUrl:String
)