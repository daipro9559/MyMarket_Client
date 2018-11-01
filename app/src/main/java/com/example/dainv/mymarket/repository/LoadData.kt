package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import com.example.dainv.mymarket.model.ResourceWrapper
import com.example.dainv.mymarket.util.ApiResponse
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

abstract class LoadData<ResultType, RequestType> {
    private val resultData = MediatorLiveData<ResourceWrapper<ResultType?>>()

    init {
        if (isLoadFromDb()){
            val dbSource = loadFromDB()
            resultData.addSource(dbSource){
                setValue(ResourceWrapper.success(it))
                resultData.removeSource(dbSource)
                resultData.addSource(getCallService()){
                    if (it!!.code<300){
                        val result = processResponse(it)
                        async (CommonPool){
                            saveToDatabase(result)
                        }
                        setValue(ResourceWrapper.success(processResponse(it)))
                    }else{
                        setValue(ResourceWrapper.error(it.throwable!!.message!!))
                    }
                }
            }
        }else {
            resultData.value = ResourceWrapper.loading()
            resultData.addSource(getCallService()) {
                val value = processResponse(it!!)
                if (it!!.throwable == null && it.body != null) {
                    setValue(ResourceWrapper.success(value))
                } else {
                    setValue(ResourceWrapper.error("401"))
                }
            }
        }
    }

     open fun saveToDatabase(value:ResultType?){
     }

    protected abstract fun processResponse(apiResponse: ApiResponse<RequestType>): ResultType?
    private fun setValue(resource: ResourceWrapper<ResultType?>) {
        resultData.value = resource
    }
    open fun loadFromDB(): LiveData<ResultType>{
        return MutableLiveData()
    }
    open fun isLoadFromDb() :Boolean{
        return false
    }

    @MainThread
    protected abstract fun getCallService(): LiveData<ApiResponse<RequestType>>

    fun getLiveData(): LiveData<ResourceWrapper<ResultType?>> {
        return resultData
    }

}