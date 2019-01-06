package com.example.dainv.mymarket.ui.map

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import javax.inject.Inject

class LocationProvider : LifecycleObserver {

    private var client: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var googleApiClient: GoogleApiClient? = null
    private lateinit var context: Context
    @Inject
    constructor(context: Context){
        this.context = context
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){

    }

}