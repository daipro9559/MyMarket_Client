package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.entity.Notification
import com.google.gson.annotations.Expose

data class NotificationResponse(
        @Expose
        val success: Boolean,
        @Expose
        val message: String,
        @Expose
        val lastPage: Boolean,
        @Expose
        val data: List<Notification>
)
