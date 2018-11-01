package com.example.dainv.mymarket.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

data class Address(
        @Expose
        var addressID:Int,
        @Expose
        var address:String,
        @Expose
        var districtID:Int):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt())
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(addressID)
        parcel.writeString(address)
        parcel.writeInt(districtID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }
}