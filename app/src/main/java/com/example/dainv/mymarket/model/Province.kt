package com.example.dainv.mymarket.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

data class Province(
        @Expose
        val provinceID:Int,
        @Expose
        val name:String
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(provinceID)
                parcel.writeString(name)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Province> {
                override fun createFromParcel(parcel: Parcel): Province {
                        return Province(parcel)
                }

                override fun newArray(size: Int): Array<Province?> {
                        return arrayOfNulls(size)
                }
        }
}