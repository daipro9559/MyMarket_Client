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
        var distance:Double,
        @Expose
        val address: String?,
        @Expose
        var isMarked: Boolean = false,
        @Expose
        val standID :String?,
        @Expose
        val provinceName :String?,
        @Expose
        var isDone:Boolean,
        @Expose
        var updatedAt:String
) : Parcelable {
     constructor(parcel: Parcel) : this(
             parcel.readString(),
             parcel.readString(),
             parcel.readLong(),
             parcel.readString(),
             parcel.createStringArrayList(),
             parcel.readByte() != 0.toByte(),
             parcel.readInt(),
             parcel.readInt(),
             parcel.readString(),
             parcel.readString(),
             parcel.readDouble(),
             parcel.readString(),
             parcel.readByte() != 0.toByte(),
             parcel.readString(),
             parcel.readString(),
             parcel.readByte() != 0.toByte(),
             parcel.readString()) {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeString(itemID)
         parcel.writeString(name)
         parcel.writeLong(price)
         parcel.writeString(description)
         parcel.writeStringList(images)
         parcel.writeByte(if (needToSell) 1 else 0)
         parcel.writeInt(categoryID)
         parcel.writeInt(addressID)
         parcel.writeString(userID)
         parcel.writeString(districtName)
         parcel.writeDouble(distance)
         parcel.writeString(address)
         parcel.writeByte(if (isMarked) 1 else 0)
         parcel.writeString(standID)
         parcel.writeString(provinceName)
         parcel.writeByte(if (isDone) 1 else 0)
         parcel.writeString(updatedAt)
     }

     override fun describeContents(): Int {
         return 0
     }

     companion object CREATOR : Parcelable.Creator<ItemMap> {
         override fun createFromParcel(parcel: Parcel): ItemMap {
             return ItemMap(parcel)
         }

         override fun newArray(size: Int): Array<ItemMap?> {
             return arrayOfNulls(size)
         }
     }
 }