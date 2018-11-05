package com.example.dainv.mymarket.model

class FilterParam(val categoryID: Int?,
                  val districtID: Int?,
                  val provinceID: Int?,
                  val needToSell: Boolean,
                  val needToBuy: Boolean,
                  val isNewest: Boolean,
                  val priceMax: Long,
                  val priceMin: Long
) {


    class Builder {
        private var categoryID: Int? = 0
        private var districtID: Int? = 0
        private var provinceID: Int? = 0
        private var needToBuy: Boolean = false
        private var needToSell = true
        private var isNewest = true
        private var priceMax: Long = 0
        private var priceMin: Long = 0
        private var priceDown = false
        private var priceUp = false

        fun setCategory(categoryID: Int?) = apply {
            this.categoryID = categoryID
        }

        fun setDistrict(districtID: Int?) = apply {
            this.districtID = districtID
        }

        fun setProvince(provinceID: Int?) = apply {
            this.provinceID = provinceID
        }

        fun setNeedToSell(needToSell: Boolean) = apply {
            this.needToSell = needToSell
        }

        fun setNeedToBuy(needToBuy: Boolean) = apply { this.needToBuy = needToBuy }
        fun setIsNewest(isNewest: Boolean) = apply { this.isNewest = isNewest }
        fun setPriceMax(priceMax: Long) = apply { this.priceMax = priceMax }
        fun setPriceMin(priceMin: Long) = apply { this.priceMin = priceMin }
        fun setPriceUp(priceUp: Boolean) = apply { this.priceUp = priceUp }
        fun setPriceDown(priceDown: Boolean) = apply { this.priceDown = priceDown }
        fun build(): FilterParam {
            return FilterParam(categoryID, districtID, provinceID, needToSell, needToBuy, isNewest, priceMax, priceMin)
        }
    }
}