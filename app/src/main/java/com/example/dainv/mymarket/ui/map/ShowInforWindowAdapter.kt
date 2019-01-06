package com.example.dainv.mymarket.ui.map

import android.content.Context
import android.view.View
import com.example.dainv.mymarket.entity.Item
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class ShowInforWindowAdapter : GoogleMap.InfoWindowAdapter {
    private val item : Item
    private val context:Context
    constructor(context: Context,item: Item){
        this.item = item
        this.context = context
    }

    override fun getInfoContents(p0: Marker?): View {
        return View(context)
    }

    override fun getInfoWindow(p0: Marker?) = null
}