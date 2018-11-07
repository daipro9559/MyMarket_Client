package com.example.dainv.mymarket.ui.items

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.model.FilterParam
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class ListItemPresenterImp @Inject constructor(
        val sharePreferencHelper: SharePreferencHelper,
        val view: ListItemView,
        val context: Context
) : ListItemPresenter, LifecycleObserver {
    override fun onCreate() {
        val provinceName = sharePreferencHelper.getString(Constant.PROVINCE_NAME, null)
        val districtName = sharePreferencHelper.getString(Constant.DISTRICT_NAME, null)
        val provinceNameShow = provinceName ?: context.getString(R.string.all_province)
        val districtNameShow = districtName ?: context.getString(R.string.all_district)
        view.setDefault(provinceNameShow, districtNameShow)
    }

    override fun saveAndSubmit() {
    }


    override fun submit(filterParam: FilterParam, isLoadPreference: Boolean) {
        val queryMap = HashMap<String, String>()
        val categoryID = filterParam.categoryID ?: sharePreferencHelper.getInt(Constant.CATEGORY_ID)
        if (categoryID > 0) {
            queryMap["categoryID"] = categoryID.toString()
        }
        var districtId: Int? = if (isLoadPreference) {
            filterParam.districtID
                    ?: sharePreferencHelper.getInt(Constant.DISTRICT_ID)
        } else {
            filterParam.districtID
        }
        if (districtId != null) {
            if (districtId > 0) {
                queryMap["districtID"] = districtId.toString()
            } else {
                val provinceID = filterParam.provinceID?: sharePreferencHelper.getInt(Constant.PROVINCE_ID)
                if (provinceID>0){
                    queryMap["provinceID"] = provinceID.toString()
                }
            }
        }
        queryMap["isNewest"] = filterParam.isNewest.toString()
        filterParam.priceMax?.let {
            queryMap["priceMax"] = it.toString()
        }
        filterParam.priceMin?.let {
            queryMap["priceMin"] = it.toString()
        }
        if (filterParam.needToBuy || filterParam.needToSell) {
            queryMap["needToSell"] = filterParam.needToSell.toString()
        }
       if (filterParam.query!=null && filterParam.query.isNotEmpty()){
            queryMap["name"] = filterParam.query
        }
//        if (filterParam.districtID != null)
        view.submit(queryMap)
    }
}