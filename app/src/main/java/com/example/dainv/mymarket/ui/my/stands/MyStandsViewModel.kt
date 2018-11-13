package com.example.dainv.mymarket.ui.my.stands

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.StandRepository
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class MyStandsViewModel
@Inject
constructor(
        private val userRepository: UserRepository,
        private val standRepository: StandRepository
) : ViewModel() {
    private val updateToSellerTrigger = MutableLiveData<Any>()
    private val listStandTrigger = MutableLiveData<Any>()
     val updateToSellerResult = Transformations.switchMap(updateToSellerTrigger){
        return@switchMap userRepository.updateToSeller()
    }
    val listMyStand = Transformations.switchMap(listStandTrigger){
        return@switchMap standRepository.getMyStands()
    }
    init {

    }

    fun updateToSeller(){
        updateToSellerTrigger.value = Any()
    }
    fun getMyStands(){
        listStandTrigger.value = Any()
    }
}