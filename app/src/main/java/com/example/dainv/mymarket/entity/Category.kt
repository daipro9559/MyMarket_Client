package com.example.dainv.mymarket.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

@Entity
data class Category(
        @PrimaryKey
        @Expose
        var categoryID:Int,
        @Expose
        var categoryName:String,
        @Expose
        var imagePath:String
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(categoryID)
                parcel.writeString(categoryName)
                parcel.writeString(imagePath)
        }

        override fun describeContents(): Int {
                return 0
        }
        companion object CREATOR : Parcelable.Creator<Category> {
                override fun createFromParcel(parcel: Parcel): Category {
                        return Category(parcel)
                }

                override fun newArray(size: Int): Array<Category?> {
                        return arrayOfNulls(size)
                }
        }

}