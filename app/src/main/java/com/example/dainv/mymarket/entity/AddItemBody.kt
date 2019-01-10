package com.example.dainv.mymarket.entity

import android.os.Parcel
import android.os.Parcelable
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
        val provinceID: Int,
        @Expose
        val latitude: Double = 0.0,
        @Expose
        val longitude: Double = 0.0,
        //user for update
        @Expose
        var itemId: String? = null,
        @Expose
        var isDeleteOldImage: Boolean = false
) : Parcelable {
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
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()
    ) {
    }

    class Builder {
        private lateinit var name: String
        private var price: Int = 0
        private lateinit var description: String
        private var categoryID: Int = 0
        private var address: String? = null
        private var itemId: String? = null
        private var districtID: Int = 0
        private var needToSell: Boolean = true
        private var isDeleteOldImage: Boolean = false
        private var standID: String? = null
        private var addressID: Int? = null
        private var provinceID: Int = 0
        private var latitude: Double = 0.0
        private var longitude: Double = 0.0


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

        fun setLatitude(latitude: Double) = apply {
            this.latitude = latitude
        }

        fun setLongitude(longitude: Double) = apply {
            this.longitude = longitude
        }

        fun setItemId(itemId: String?) = apply {
            this.itemId = itemId
        }

        fun setIsDeleteImage(isDeleteOldImage: Boolean) = apply {
            this.isDeleteOldImage = isDeleteOldImage
        }

        fun build(): AddItemBody {
            return AddItemBody(name, price, description, categoryID
                    , address, districtID, needToSell, standID
                    , addressID, provinceID, latitude, longitude
                    , itemId, isDeleteOldImage)
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