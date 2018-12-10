package com.example.dainv.mymarket.ui.transaction

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.StandRepository
import com.example.dainv.mymarket.repository.TransactionRepository
import com.google.gson.annotations.Expose
import retrofit2.http.QueryMap
import javax.inject.Inject

class TransactionViewModel
    @Inject
    constructor(private val transactionRepository: TransactionRepository,
                private val standRepository: StandRepository)
    : ViewModel(){
    private val queryMapTrigger = MutableLiveData<Map<String,String>>()
    private val addItemToStandParamTrigger= MutableLiveData<AddItemToStandParam>()
    val listTransactionLiveData = Transformations.switchMap(queryMapTrigger){
        transactionRepository.getTransaction(it)
    }!!
    val  addItemToStandResult = Transformations.switchMap(addItemToStandParamTrigger){
        standRepository.addItemFromTransaction(it.standID,it.itemID)
    }
    fun getTransaction(queryMap:Map<String,String>){
        queryMapTrigger.value = queryMap
    }
    fun addItemToStand(itemID:String,standID:String){
        addItemToStandParamTrigger.value  = AddItemToStandParam(standID,itemID)
    }
   data class AddItemToStandParam(
            val standID:String,
            val itemID:String)
}