package com.example.dainv.mymarket.ui.items

import com.example.dainv.mymarket.entity.FilterParam

interface ListItemPresenter {
    fun onCreate()
    fun submit(filterParam: FilterParam,isLoadSharePreference :Boolean = false)
    fun saveAndSubmit()
}