package com.example.dainv.mymarket.ui.main.notifications

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.dainv.mymarket.repository.NotificationRepository
import javax.inject.Inject

class NotificationViewModel
    @Inject
    constructor(
            val notificationRepository: NotificationRepository
    )
    :ViewModel() {
    private val pageNotificationTrigger = MutableLiveData<Int>()
    private val deleteTrigger = MutableLiveData<String>()
    val notificationsLiveData = Transformations.switchMap(pageNotificationTrigger){
        notificationRepository.getAllNotification(it)
    }!!
    val deleteResultData = Transformations.switchMap(deleteTrigger){
        notificationRepository.deleteNotification(it)
    }
    init {
        getNotification(0)
    }

    fun getNotification(page:Int){
        pageNotificationTrigger.value = page
    }

    fun deleteNotification(notificationId:String){
        deleteTrigger.value = notificationId
    }


}