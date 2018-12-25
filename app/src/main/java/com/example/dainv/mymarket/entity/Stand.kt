package com.example.dainv.mymarket.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

data class Stand(
        @Expose
        val standID: String,
        @Expose
        val name: String,
        @Expose
        val image: List<String>,
        @Expose
        val description: String,
        @Expose
        val categoryID: Int,
        @Expose
        val Address: Address?,
        @Expose
        val userID:String?,
        @Expose
        var isFollowed:Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readParcelable(com.example.dainv.mymarket.entity.Address::class.java.classLoader),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(standID)
        parcel.writeString(name)
        parcel.writeStringList(image)
        parcel.writeString(description)
        parcel.writeInt(categoryID)
        parcel.writeParcelable(Address, flags)
        parcel.writeString(userID)
        parcel.writeByte(if (isFollowed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stand> {
        override fun createFromParcel(parcel: Parcel): Stand {
            return Stand(parcel)
        }

        override fun newArray(size: Int): Array<Stand?> {
            return arrayOfNulls(size)
        }
    }
}