package com.example.dainv.mymarket.ui.items

import retrofit2.http.QueryMap

interface ListItemView {
    fun submit(queryMap: Map<String, String>)
    // show dialog error
    fun error(message:String)

    fun setDefault(provinceName:String,districtName:String)
}