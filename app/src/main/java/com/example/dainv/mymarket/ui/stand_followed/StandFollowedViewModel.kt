package com.example.dainv.mymarket.ui.stand_followed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.StandRepository
import javax.inject.Inject

class StandFollowedViewModel
    @Inject
    constructor(private val standRepository: StandRepository)
    : ViewModel(){
    private val standsFollowedTrigger = MutableLiveData<Any>()

    val standsFollowed = Transformations.switchMap(standsFollowedTrigger){
        standRepository.getStandsFollowed()
    }

    fun getStandsFollowed(){
        standsFollowedTrigger.value = Any()
    }
}