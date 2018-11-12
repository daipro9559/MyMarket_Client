package com.example.dainv.mymarket.ui.itemdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class ItemDetailViewModel @Inject constructor(val userRepository: UserRepository): ViewModel() {
    private val sellerIDLiveData = MutableLiveData<String>()
    var phoneDataLiveData = Transformations.switchMap(sellerIDLiveData){
        userRepository.getPhoneSeller(it)
    }!!
    fun getPhone(sellerID:String){
        sellerIDLiveData.value = sellerID
    }
}