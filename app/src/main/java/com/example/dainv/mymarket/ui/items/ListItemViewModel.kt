package com.example.dainv.mymarket.ui.items

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.repository.AddressRepository
import com.example.dainv.mymarket.repository.ItemRepository
import com.example.dainv.mymarket.util.SharePreferencHelper
import javax.inject.Inject

class ListItemViewModel
    @Inject constructor(private val itemRepository: ItemRepository,
                        private val addressRepository: AddressRepository
                        ): ViewModel() {
    private val itemIdMark = MutableLiveData<String>()
    private val itemIdUnMark = MutableLiveData<String>()
    private val deleteTrigger = MutableLiveData<String>()
    private val queryMap = MutableLiveData<Map<String,String>>()
    val listItemLiveData = Transformations.switchMap(queryMap){
        return@switchMap itemRepository.getItems(it)
    }
    val itemMarkResult = Transformations.switchMap(itemIdMark){
        return@switchMap itemRepository.markItem(it)
    }
    val itemUnmarkResult = Transformations.switchMap(itemIdUnMark){
        return@switchMap itemRepository.unMarkItem(it)
    }
    val errorLiveData = itemRepository.errorLiveData

    val deleteResult = Transformations.switchMap(deleteTrigger){
        return@switchMap itemRepository.delete(it)
    }

    fun getItem(map: Map<String,String>){
        queryMap.value = map
    }

    fun deleteItem(itemId:String){
        deleteTrigger.value = itemId
    }
    fun markItem(itemId:String){
        itemIdMark.value = itemId
    }
    fun unMarkItem(itemId: String){
        itemIdUnMark.value = itemId
    }
    fun getAllCategory()= itemRepository.getAllCategory()
    fun getAllProvince() = addressRepository.getAllProvince()
    fun getDistrics(provinceID:Int)= addressRepository.getAllDistrict(provinceID)

}