package com.example.dainv.mymarket.ui.stand.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import com.example.dainv.mymarket.repository.StandRepository
import javax.inject.Inject

class StandDetailViewModel
    @Inject constructor(private val standRepository: StandRepository,
                        private val itemRepository: ItemRepository) :ViewModel() {
    private val queryMap = MutableLiveData<Map<String,String>>()
    val listItemLiveData = Transformations.switchMap(queryMap){
        return@switchMap itemRepository.getItems(it)
    }
    fun getItem(map: Map<String,String>){
        queryMap.value = map
    }
}