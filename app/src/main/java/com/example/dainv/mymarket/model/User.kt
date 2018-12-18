package com.example.dainv.mymarket.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
//@Entity
data class User(
        @PrimaryKey
        @Expose
        val userID: String,
        @Expose
        val name: String,
        @Expose
        val email:String,
        @Expose
        val phone : String,
        @Expose
        val avatar:String,
        @Expose
        val userType:Int,
        @Expose
        val userRoleID:Int,
        @Expose
        val createdAt:String,
        @Expose
        val Address:Address
)