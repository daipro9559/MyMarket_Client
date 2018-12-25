package com.example.dainv.mymarket.entity

import com.google.gson.annotations.Expose

data class Transaction(
        @Expose
        val transactionID:String,
        @Expose
        val sellerID:String,
        @Expose
        val  buyerID:String,
        @Expose
        val price:Long,
        @Expose
        val Item:Item,
        @Expose
        val createdAt:String,
        @Expose
        val updatedAt:String
)