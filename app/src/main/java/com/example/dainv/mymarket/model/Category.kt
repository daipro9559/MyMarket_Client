package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class Category(
        @Expose
        val categoryID:Int,
        @Expose
        val categoryName:String,
        @Expose
        val imagePath:String
)