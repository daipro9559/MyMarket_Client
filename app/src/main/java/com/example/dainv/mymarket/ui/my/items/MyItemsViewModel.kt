package com.example.dainv.mymarket.ui.my.items

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import retrofit2.http.QueryMap
import javax.inject.Inject

class MyItemsViewModel
    @Inject
    constructor(
           private val  itemRepository: ItemRepository
    )
    : ViewModel() {
    init {
//        val queryMap = HashMap<String,String>()
//        queryMap["page"] = 0.toString()
//        queryMap["isMyItems"] = true.toString()
//        getItem(queryMap)
    }
    private val deleteTrigger = MutableLiveData<String>()
    val deleteResult = Transformations.switchMap(deleteTrigger){
        return@switchMap itemRepository.delete(it)
    }
    private val queryMap = MutableLiveData<Map<String,String>>()
    val listItemLiveData = Transformations.switchMap(queryMap){
        return@switchMap itemRepository.getItems(it)
    }
    fun getItem(map: Map<String,String>){
        queryMap.value = map
    }
    fun deleteItem(itemId:String){
        deleteTrigger.value = itemId
    }
}