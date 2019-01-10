package com.example.dainv.mymarket.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

data class Address(
        @Expose
        var addressID:Int,
        @Expose
        var address:String,
        @Expose
        var districtID:Int,
        @Expose
        var District:DistrictData?,
        @Expose
        var latitude:Double?,
        var longitude:Double?
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readParcelable(DistrictData::class.java.classLoader),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(addressID)
        parcel.writeString(address)
        parcel.writeInt(districtID)
        parcel.writeParcelable(District, flags)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
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

data class DistrictData(
           @Expose
           var districtID:Int,
           @Expose
           var districtName:String,
           @Expose
           var provinceID:Int,
           @Expose
           var Province:Province?
   ):Parcelable{
       constructor(parcel: Parcel) : this(
               parcel.readInt(),
               parcel.readString(),
               parcel.readInt(),
               parcel.readParcelable(com.example.dainv.mymarket.entity.Province::class.java.classLoader)) {
       }

       override fun writeToParcel(parcel: Parcel, flags: Int) {
           parcel.writeInt(districtID)
           parcel.writeString(districtName)
           parcel.writeInt(provinceID)
           parcel.writeParcelable(Province, flags)
       }

       override fun describeContents(): Int {
           return 0
       }

       companion object CREATOR : Parcelable.Creator<DistrictData> {
           override fun createFromParcel(parcel: Parcel): DistrictData {
               return DistrictData(parcel)
           }

           override fun newArray(size: Int): Array<DistrictData?> {
               return arrayOfNulls(size)
           }
       }

   }
