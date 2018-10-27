package com.example.dainv.mymarket.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken

data class Item(
        @Expose
        val itemID:Int,
        @Expose
        var name:String,
        @Expose
        var price:Int,
        @Expose
        var description:String,
        @Expose
        val images:List<String>,
        @Expose
        var needToSale:Boolean,
        @Expose
        var categoryID:Int,
        @Expose
        var addressID:Int,
        @Expose
        var userID:Int,
        @Expose
        val Address:Address?): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString(),
                parcel.createStringArrayList(),
                parcel.readByte() != 0.toByte(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readParcelable(com.example.dainv.mymarket.model.Address::class.java.classLoader))

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(itemID)
                parcel.writeString(name)
                parcel.writeInt(price)
                parcel.writeString(description)
                parcel.writeStringList(images)
                parcel.writeByte(if (needToSale) 1 else 0)
                parcel.writeInt(categoryID)
                parcel.writeInt(addressID)
                parcel.writeInt(userID)
                parcel.writeParcelable(Address, flags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Item> {
                override fun createFromParcel(parcel: Parcel): Item {
                        return Item(parcel)
                }
                override fun newArray(size: Int): Array<Item?> {
                        return arrayOfNulls(size)
                }
        }
}