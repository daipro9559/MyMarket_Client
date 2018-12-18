package com.example.dainv.mymarket.ui.addAddress

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.AddressRepository
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class AddAdressViewModel
@Inject
constructor(
        private val addressRepository: AddressRepository,
        private val userRepository: UserRepository
) : ViewModel() {

    private val addAddressTrigger = MutableLiveData<AddAddressParam>()

    val addAddressResult = Transformations.switchMap(addAddressTrigger){
        userRepository.addAddress(it)
    }

    fun confirmAddress(address:String,provinceID: Int,districtID: Int){
        addAddressTrigger.value = AddAddressParam(provinceID,districtID,address)
    }
    data class AddAddressParam(
            val provinceID :Int,
            val districtID : Int,
            val address: String
    )
}