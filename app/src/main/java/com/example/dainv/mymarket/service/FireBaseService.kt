package com.example.dainv.mymarket.service

import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.util.SharePreferencHelper
import com.example.dainv.mymarket.util.Util
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class FireBaseService
    : FirebaseMessagingService() {
    @Inject
    lateinit var sharePreferencHelper: SharePreferencHelper

    private val CHANNEL_ID ="new item upload"
    private lateinit var notifyBuilder: NotificationCompat.Builder
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }


    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        // save new tokenFireBase
        sharePreferencHelper.putString(Constant.TOKEN_FIREBASE,p0)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        p0?.data?.let {
            Timber.e(it.toString())
            val json = JSONObject(p0.data)
            showNotification(json,p0.notification?.title,p0.notification?.body)
        }

    }

    private fun showNotification(jsonObject: JSONObject,  title:String?, message:String?){
        val  intentItemDetail = Util.buildIntentForNotification(jsonObject,this)
        val pIntent = PendingIntent.getActivity(this, 0, intentItemDetail, PendingIntent.FLAG_UPDATE_CURRENT)
        val code = System.currentTimeMillis().toInt()
        notifyBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_app)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        NotificationManagerCompat.from(this)
                .notify(code,notifyBuilder.build())
    }
}