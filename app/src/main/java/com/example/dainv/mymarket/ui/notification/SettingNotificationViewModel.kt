package com.example.dainv.mymarket.ui.notification

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.entity.*
import com.example.dainv.mymarket.repository.AddressRepository
import com.example.dainv.mymarket.repository.ItemRepository
import com.example.dainv.mymarket.repository.NotificationRepository
import javax.inject.Inject

class SettingNotificationViewModel
    @Inject
    constructor(
            private val notificationRepository: NotificationRepository,
            private val addressRepository: AddressRepository,
            private val itemRepository: ItemRepository
    )
    :ViewModel() {
    private val idCategory = MutableLiveData<Int>()
    private val idDistrict = MutableLiveData<Int>()
    private val idProvince = MutableLiveData<Int>()
    private val saveSettingTrigger = MutableLiveData<NotificationSetting>()
    private val getNotificationSettingTrigger = MutableLiveData<Any>()
    val getSettingResult = Transformations.switchMap(getNotificationSettingTrigger){
        notificationRepository.getSetting()
    }!!
    val saveSettingResult = Transformations.switchMap(saveSettingTrigger){
        notificationRepository.saveSetting(it)
    }!!
    // trigger for get all district
    private val provinceId= MutableLiveData<Int>()
    val listDistrictLiveData = Transformations.switchMap(provinceId){
        return@switchMap addressRepository.getAllDistrict(it)
    }!!

    val districtLiveData = Transformations.switchMap(idDistrict){
        addressRepository.getDistrict(it)
    }

    val provinceLiveData =  Transformations.switchMap(idProvince){
        addressRepository.getProvince(it)
    }
    val categoryLiveData = Transformations.switchMap(idCategory){
        itemRepository.getCategory(it)
    }
    init {
        getSetting()
    }
     fun getAllCategory(): LiveData<ResourceWrapper<List<Category>?>> {
        return itemRepository.getAllCategory()
    }
     fun getAllProvince() : LiveData<ResourceWrapper<List<Province>?>> {
        return addressRepository.getAllProvince()
    }
     fun getDistricts(provinceID:Int){
         this.provinceId.value = provinceID
    }

    fun getDistrict(id :Int){

    }
    fun getProvince(id :Int){

    }
    fun getCategory(id :Int){

    }

    fun saveSetting(notificationSetting: NotificationSetting){
        saveSettingTrigger.value = notificationSetting
    }
    fun getSetting(){
        getNotificationSettingTrigger.value = Any()
    }
}