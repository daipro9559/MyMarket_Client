package com.example.dainv.mymarket.ui.stand.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.ItemRepository
import com.example.dainv.mymarket.repository.StandRepository
import com.example.dainv.mymarket.repository.UserRepository
import javax.inject.Inject

class StandDetailViewModel
    @Inject constructor(private val standRepository: StandRepository,
                        private val userRepository: UserRepository,
                        private val itemRepository: ItemRepository) :ViewModel() {
    private val queryMap = MutableLiveData<Map<String,String>>()
    private val profileTrigger = MutableLiveData<String>()
    private val followTrigger = MutableLiveData<String>()
    private val unFollowTrigger = MutableLiveData<String>()
    private val deleteTrigger = MutableLiveData<String>()
    val listItemLiveData = Transformations.switchMap(queryMap){
        return@switchMap itemRepository.getItems(it)
    }!!
    val profileResult = Transformations.switchMap(profileTrigger){
        return@switchMap userRepository.getProfile(it)
    }!!
    val followResult = Transformations.switchMap(followTrigger){
        return@switchMap standRepository.follow(it)
    }
    val unFollowResult = Transformations.switchMap(unFollowTrigger){
        return@switchMap standRepository.unFollow(it)
    }
    val deleteResult = Transformations.switchMap(deleteTrigger){
        standRepository.delete(it)
    }
    fun getItem(map: Map<String,String>){
        queryMap.value = map
    }
    fun getUserProfile(userID:String){
        profileTrigger.value = userID
    }

    fun follow(standID:String){
        followTrigger.value = standID
    }
    fun unFollow(standID:String){
        unFollowTrigger.value = standID
    }
    fun deleteStand(standID: String){
        deleteTrigger.value = standID
    }
}