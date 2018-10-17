package com.example.dainv.mymarket.ui.items

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import retrofit2.http.QueryMap
import javax.inject.Inject

class ListItemViewModel
    @Inject constructor(private val itemRepository: ItemRepository): ViewModel() {
    private val queryMap = MutableLiveData<Map<String,String>>()
    val listItemLiveData = Transformations.switchMap(queryMap){
        return@switchMap itemRepository.getItems(it)
    }
    val errorLiveData = itemRepository.errorLiveData

    fun getItem(map: Map<String,String>){
        queryMap.value = map
    }
}