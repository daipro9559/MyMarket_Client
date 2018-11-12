package com.example.dainv.mymarket.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
@Entity
data class Item(
        @PrimaryKey
        @Expose
        val itemID:String,
        @Expose
        var name:String,
        @Expose
        var price:Long,
        @Expose
        var description:String,
        @Expose
        val images:List<String> = ArrayList(),
        @Expose
        var needToSell:Boolean,
        @Expose
        var categoryID:Int,
        @Expose
        var addressID:Int,
        @Expose
        var userID:String,
        @Expose
        val Address:Address?,
        @Expose
        var isMarked:Boolean = false): Parcelable {
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
                parcel.readParcelable(com.example.dainv.mymarket.model.Address::class.java.classLoader))
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