package com.example.dainv.mymarket.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose


data class Category(
        @Expose
        val categoryID:Int,
        @Expose
        val categoryName:String,
        @Expose
        val imagePath:String
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