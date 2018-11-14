package com.example.dainv.mymarket.service

import com.example.dainv.mymarket.util.SharePreferencHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import javax.inject.Inject

class FireBaseService
    : FirebaseMessagingService() {
    @Inject
    lateinit var sharePreferencHelper: SharePreferencHelper
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }


    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        p0?.data?.let {
            if (it.isNotEmpty()){

            }
        }
        
    }
}