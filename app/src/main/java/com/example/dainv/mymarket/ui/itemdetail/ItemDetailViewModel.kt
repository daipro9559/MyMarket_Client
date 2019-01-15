package com.example.dainv.mymarket.ui.itemdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import com.example.dainv.mymarket.repository.NotificationRepository
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class ItemDetailViewModel @Inject constructor(val userRepository: UserRepository,
                                              val itemRepository: ItemRepository,
                                              val notificationRepository: NotificationRepository) : ViewModel() {
    private val sellerIDLiveData = MutableLiveData<String>()
    private val getItemIdTrigger = MutableLiveData<String>()
    private val requestBuyParamTrigger = MutableLiveData<RequestBuyParam>()

    var phoneDataLiveData = Transformations.switchMap(sellerIDLiveData) {
        return@switchMap userRepository.getPhoneSeller(it)
    }!!
    val itemDetailResult = Transformations.switchMap(getItemIdTrigger) {
        return@switchMap itemRepository.getItemDetail(it)
    }!!

    val requestBuyResult = Transformations.switchMap(requestBuyParamTrigger){
        notificationRepository.requestBuy(it.itemID,it.userID,it.itemName,it.price)
    }!!

    fun getPhone(sellerID: String) {
        sellerIDLiveData.value = sellerID
    }

    fun getItemDetail(itemId: String) {
        getItemIdTrigger.value = itemId
    }

    fun requestBuyItem(itemID: String, userID:String,itemName:String,price:Long){
        requestBuyParamTrigger.value = RequestBuyParam(itemID,userID,itemName,price)
    }

    data class RequestBuyParam(
            val itemID:String,
            val userID:String,
            val itemName:String,
            val price:Long
    )
}