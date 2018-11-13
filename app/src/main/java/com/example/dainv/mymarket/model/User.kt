package com.example.dainv.mymarket.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
data class User(
        @Expose
        val userID: String,
        @Expose
        val name: String,
        @Expose
        val email:String,
        @Expose
        val phone : String,
        @Expose
        val userType:Int,
        @Expose
        val userRoleID:Int
)