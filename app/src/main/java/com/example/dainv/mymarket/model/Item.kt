package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class Item(
        @Expose
        val itemID:Int,
        @Expose
        var name:String,
        @Expose
        var price:Int,
        @Expose
        var desctiption:String,
        @Expose
        val imagePath:String,
        @Expose
        var needToSale:Boolean,
        @Expose
        var categoryID:Int,
        @Expose
        var addressID:Int,
        @Expose
        var userID:Int

)