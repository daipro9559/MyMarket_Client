package com.example.dainv.mymarket.ui.main.stands

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.StandRepository
import javax.inject.Inject

class StandsViewModel
    @Inject
    constructor(private val standRepository: StandRepository)
    :ViewModel() {

    private val listStandTrigger = MutableLiveData<Int>()

    val listStandLiveData = Transformations.switchMap(listStandTrigger){
        return@switchMap standRepository.getStands()
    }
    init {
        getStands(0)
    }
    fun getStands(page:Int){
        listStandTrigger.value = page
    }
}