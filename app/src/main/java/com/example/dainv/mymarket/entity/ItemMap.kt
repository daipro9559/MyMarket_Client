package com.example.dainv.mymarket.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

data class ItemMap(
        @Expose
        val itemID: String,
        @Expose
        var name: String,
        @Expose
        var price: Long,
        @Expose
        var description: String,
        @Expose
        val images: List<String> = ArrayList(),
        @Expose
        var needToSell: Boolean,
        @Expose
        var categoryID: Int,
        @Expose
        var addressID: Int,
        @Expose
        var userID: String,
        @Expose
        var districtName: String,
        @Expose
        var distance: Double,
        @Expose
        val address: String?,
        @Expose
        var isMarked: Boolean = false,
        @Expose
        val standID: String?,
        @Expose
        val provinceName: String?,
        @Expose
        var isDone: Boolean,
        @Expose
        var updatedAt: String,
        @Expose
        val latitude: Double,
        @Expose
        val longitude : Double
)