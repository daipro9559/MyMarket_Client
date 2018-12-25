package com.example.dainv.mymarket.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
@Entity(foreignKeys = [ForeignKey(entity = Province::class, parentColumns = arrayOf("provinceID"), childColumns = arrayOf("provinceID"))])
data class District(
       @PrimaryKey
        @Expose
        var districtID:Int,
        @Expose
        var districtName:String,
       @Expose
       @ColumnInfo(name = "provinceID")
       var provinceID:Int
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(districtID)
                parcel.writeString(districtName)
                parcel.writeInt(provinceID)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<District> {
                override fun createFromParcel(parcel: Parcel): District {
                        return District(parcel)
                }

                override fun newArray(size: Int): Array<District?> {
                        return arrayOfNulls(size)
                }
        }
}