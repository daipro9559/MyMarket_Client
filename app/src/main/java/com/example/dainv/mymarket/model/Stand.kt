package com.example.dainv.mymarket.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

data class Stand(
        @Expose
        val standID:String,
        @Expose
        val name:String,
        @Expose
        val image:String,
        @Expose
        val description:String,
        @Expose
        val categoryID:Int
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(standID)
                parcel.writeString(name)
                parcel.writeString(image)
                parcel.writeString(description)
                parcel.writeInt(categoryID)
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