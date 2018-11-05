package com.example.dainv.mymarket.ui.items

import com.example.dainv.mymarket.model.FilterParam

interface ListItemPresenter {
    // get param from sharePreference
    fun onCreate()
    fun submit(filterParam: FilterParam,isLoadSharePreference :Boolean = false)
    fun saveAndSubmit()
}