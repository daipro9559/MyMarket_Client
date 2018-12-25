package com.example.dainv.mymarket.api.response

import com.example.dainv.mymarket.entity.User
import com.google.gson.annotations.Expose

data class ListUserResponse (
    @Expose
    val success:Boolean,
    @Expose
    val message:String,
    @Expose
    val lastPage: Boolean,
    @Expose
    val data:List<User>)
