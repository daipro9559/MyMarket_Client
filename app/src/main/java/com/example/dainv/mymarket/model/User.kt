package com.example.dainv.mymarket.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
@Entity
data class User(
        @PrimaryKey
        @Expose
        val userID: String,
        @ColumnInfo(name = "userName")
        @Expose
        val name: String,
        @Expose
        @ColumnInfo(name="email")
        val email:String,
        @ColumnInfo(name="phone")
        @Expose
        val phone : String,
        @Expose
        val userType:Int,
        @Expose
        val userRoleID:Int
)