package com.example.dainv.mymarket.base

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.util.ErrorResponseLiveData

public abstract class BaseRepository {
    var errorLiveData =  ErrorResponseLiveData()

    open fun hanlderCallService(){

    }

    open fun handlerErr(throwable: Throwable){

    }
}