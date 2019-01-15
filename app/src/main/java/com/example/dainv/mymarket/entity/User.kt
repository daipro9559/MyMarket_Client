package com.example.dainv.mymarket.entity

import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
//@Entity
data class User(
        @PrimaryKey
        @Expose
        val userID: String?,
        @Expose
        val name: String?,
        @Expose
        val email:String?,
        @Expose
        val phone : String?,
        @Expose
        val avatar:String?,
        @Expose
        val userType:Int?,
        @Expose
        val userRoleID:Int?,
        @Expose
        val createdAt:String?,
        @Expose
        val Address:Address?
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readString(),
                parcel.readParcelable(com.example.dainv.mymarket.entity.Address::class.java.classLoader)) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(userID)
                parcel.writeString(name)
                parcel.writeString(email)
                parcel.writeString(phone)
                parcel.writeString(avatar)
                parcel.writeValue(userType)
                parcel.writeValue(userRoleID)
                parcel.writeString(createdAt)
                parcel.writeParcelable(Address, flags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<User> {
                override fun createFromParcel(parcel: Parcel): User {
                        return User(parcel)
                }

                override fun newArray(size: Int): Array<User?> {
                        return arrayOfNulls(size)
                }
        }

}