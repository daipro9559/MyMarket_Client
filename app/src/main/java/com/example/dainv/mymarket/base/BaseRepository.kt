package com.example.dainv.mymarket.base

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.util.ErrorResponseLiveData

public abstract class BaseRepository {
    protected lateinit var errorLiveData: ErrorResponseLiveData

    protected fun hanlderCallService(){

    }
}