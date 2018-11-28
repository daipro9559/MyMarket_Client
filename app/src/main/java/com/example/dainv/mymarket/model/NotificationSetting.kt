package com.example.dainv.mymarket.model

import com.google.gson.annotations.Expose

data class NotificationSetting(
        @Expose
        val conditionID:String?,
        @Expose
        var isEnable:Boolean,
        @Expose
        var radius:Float,
        @Expose
        var District:District?,
        @Expose
        var Province:Province?,
        @Expose
        var Category:Category?

)