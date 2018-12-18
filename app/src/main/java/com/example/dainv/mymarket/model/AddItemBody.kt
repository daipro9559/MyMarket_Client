package com.example.dainv.mymarket.model

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AlertDialog
import com.google.gson.annotations.Expose

class AddItemBody(
        @Expose
        val name: String,
        @Expose
        val price: Int,
        @Expose
        val description: String,
        @Expose
        val categoryID: Int,
        @Expose
        val address: String?,
        @Expose
        val districtID: Int,
        @Expose
        val needToSell: Boolean,
        @Expose
        var standID: String? = null,
        @Expose
        var addressID: Int? = null,
        @Expose
        val provinceID: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readInt()) {
    }

    class Builder {
        private lateinit var name: String
        private var price: Int = 0
        private lateinit var description: String
        private var categoryID: Int = 0
        private var address: String? = null
        private var districtID: Int = 0
        private var needToSell: Boolean = true
        private var standID: String? = null
        private var addressID: Int? = null
        private var provinceID: Int = 0


        fun setName(name: String) = apply {
            this.name = name
        }

        fun setPrice(price: Int) = apply {
            this.price = price
        }

        fun setAddress(address: String?) = apply {
            this.address = address
        }

        fun setDescription(des: String) = apply {
            this.description = des
        }

        fun setDistrictID(districtID: Int) = apply {
            this.districtID = districtID
        }

        fun setProvinceID(provinceID: Int) = apply {
            this.provinceID = provinceID
        }

        fun setCategoryID(categoryID: Int) = apply {
            this.categoryID = categoryID
        }

        fun setNeedToSell(needToSell: Boolean) = apply {
            this.needToSell = needToSell
        }

        fun setStandId(standId: String) = apply {
            this.standID = standId
        }

        fun setAddressID(addressID: Int) = apply {
            this.addressID = addressID
        }

        fun build(): AddItemBody {
            return AddItemBody(name, price, description, categoryID, address, districtID, needToSell, standID, addressID,provinceID)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeString(description)
        parcel.writeInt(categoryID)
        parcel.writeString(address)
        parcel.writeInt(districtID)
        parcel.writeByte(if (needToSell) 1 else 0)
        parcel.writeString(standID)
        parcel.writeValue(addressID)
        parcel.writeInt(provinceID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddItemBody> {
        override fun createFromParcel(parcel: Parcel): AddItemBody {
            return AddItemBody(parcel)
        }

        override fun newArray(size: Int): Array<AddItemBody?> {
            return arrayOfNulls(size)
        }
    }
}