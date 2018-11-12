package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class Stand(
        @Expose
        val standID:String,
        @Expose
        val name:String,
        @Expose
        val image:String,
        @Expose
        val description:String,
        @Expose
        val categoryID:Int
)