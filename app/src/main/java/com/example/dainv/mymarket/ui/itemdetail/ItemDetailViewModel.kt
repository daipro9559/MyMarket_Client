package com.example.dainv.mymarket.ui.itemdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class ItemDetailViewModel @Inject constructor(val userRepository: UserRepository,
                                              val itemRepository: ItemRepository): ViewModel() {
    private val sellerIDLiveData = MutableLiveData<String>()
    private val getItemIdTrigger = MutableLiveData<String>()

    var phoneDataLiveData = Transformations.switchMap(sellerIDLiveData){
        return@switchMap userRepository.getPhoneSeller(it)
    }!!
    val itemDetailResult = Transformations.switchMap(getItemIdTrigger){
        return@switchMap itemRepository.getItemDetail(it)
    }!!
    fun getPhone(sellerID:String){
        sellerIDLiveData.value = sellerID
    }

    fun getItemDetail(itemId:String){
        getItemIdTrigger.value = itemId
    }
}