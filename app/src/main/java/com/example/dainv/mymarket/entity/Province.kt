package com.example.dainv.mymarket.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
@Entity
data class Province(
        @PrimaryKey
        @Expose
        var provinceID:Int,
        @Expose
        var provinceName:String
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(provinceID)
                parcel.writeString(provinceName)
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