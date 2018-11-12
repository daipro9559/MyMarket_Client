package com.example.dainv.mymarket.ui.main.item.marked

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import javax.inject.Inject

class ItemsMarkedViewModel
@Inject
constructor(private val itemRepository: ItemRepository) : ViewModel() {
    private val listItemMarkTrigger = MutableLiveData<Any>()
     val listItemMarkedResult = Transformations.switchMap(listItemMarkTrigger){
         return@switchMap itemRepository.getAllItemMarked()
     }
    init {
        getItemsMarked()
    }
    fun getItemsMarked() {
        listItemMarkTrigger.value = Any()
    }

}