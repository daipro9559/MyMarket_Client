package com.example.dainv.mymarket.entity

import com.google.gson.annotations.Expose

data class Notification(
        @Expose
        val notificationID: String,
        @Expose
        val type: Int,
        @Expose
        val title: String,
        @Expose
        val body: String,
        @Expose
        val icon: String,
        @Expose
        val data: String,
        @Expose
        val updatedAt:String
) {

}