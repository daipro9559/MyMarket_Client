package com.example.dainv.mymarket.entity

import com.google.gson.annotations.Expose

data class Comment(
        @Expose
        val commentID:String,
        @Expose
        val comment:String,
        @Expose
        val standID:String,
        @Expose
        val User : User,
        @Expose
        val createdAt:String,
        @Expose
        val updatedAt:String
)