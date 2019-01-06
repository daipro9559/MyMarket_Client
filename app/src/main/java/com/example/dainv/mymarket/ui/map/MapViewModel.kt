package com.example.dainv.mymarket.ui.map

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import javax.inject.Inject

class MapViewModel
    @Inject
    constructor(private val itemRepository: ItemRepository)
    : ViewModel(){
    private val queryMapTrigger = MutableLiveData<Map<String,String>>()

}