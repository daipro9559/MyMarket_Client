package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.entity.NotificationSetting
import com.google.gson.annotations.Expose

data class NotificationSettingResponse(
    @Expose
    val success: Boolean,
    @Expose
    val message: String,
    @Expose
    val lastPage: Boolean,
    @Expose
    val data: NotificationSetting
)