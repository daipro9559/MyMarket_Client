package com.example.dainv.mymarket.ui.map

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import javax.inject.Inject

class MapViewModel
    @Inject
    constructor(private val itemRepository: ItemRepository)
    : ViewModel(){
    private val queryMapTrigger = MutableLiveData<Map<String,String?>>()

    val listItem = Transformations.switchMap(queryMapTrigger){
        itemRepository.findOnMap(it)
    }

    fun findItem(queryMap:Map<String,String?>){
        queryMapTrigger.value = queryMap
    }
}