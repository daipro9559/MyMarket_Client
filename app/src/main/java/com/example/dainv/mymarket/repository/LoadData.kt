package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import com.example.dainv.mymarket.model.ResourceWrapper
import com.example.dainv.mymarket.util.ApiResponse

abstract class LoadData<ResultType,RequestType>{
    val resultData = MediatorLiveData<ResourceWrapper<ResultType>>()

    init {
        resultData.value = ResourceWrapper.loading()
        resultData.addSource(getCallService()){
            if (it!!.throwable==null){
                val value = processResponse(it)
                setValue(ResourceWrapper.success(value!!))
            }
        }
    }
    protected abstract fun processResponse(apiResponse: ApiResponse<RequestType>):ResultType?
    private fun setValue(resource : ResourceWrapper<ResultType>?){
        resultData.value = resource
    }
    protected abstract fun loadFromDB(): LiveData<ResultType>
    protected abstract fun isLoadFromDb(isForce: Boolean):Boolean

    @MainThread
    protected abstract fun getCallService():LiveData<ApiResponse<RequestType>>

}