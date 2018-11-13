package com.example.dainv.mymarket.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import com.example.dainv.mymarket.model.ResourceWrapper
import com.example.dainv.mymarket.util.ApiResponse
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import timber.log.Timber

abstract class LoadData<ResultType, RequestType> {
    private val resultData = MediatorLiveData<ResourceWrapper<ResultType?>>()

    init {
        initData()
    }

    protected open fun initData() {
        if (isLoadFromDb()) {
            val dbSource = loadFromDB()
            resultData.addSource(dbSource) {
                resultData.removeSource(dbSource)
                if (!needFetchData(it)) {
                    setValue(ResourceWrapper.success(it))
                } else {
                    val api = getCallService()
                    resultData.addSource(getCallService()) {
                        resultData.removeSource(api)
                        if (it!!.code < 300) {
                            val result = processResponse(it)
                            async(CommonPool) {
                                saveToDatabase(result)
                                withContext(UI) {
                                    resultData.addSource(loadFromDB()) {
                                        setValue(ResourceWrapper.success(result))
                                    }
                                }
                            }
                        } else {
                            setValue(ResourceWrapper.error(it.throwable!!.message!!))
                        }
                    }
                }
            }
        } else {
            resultData.value = ResourceWrapper.loading()
            val callService = getCallService()
            resultData.addSource(callService) {
                resultData.removeSource(callService)
                val value = processResponse(it!!)
                if (it!!.throwable == null && it.body != null) {
                    setValue(ResourceWrapper.success(value))
                } else {
                    setValue(ResourceWrapper.error("401"))
                }
            }
        }
    }

    open fun saveToDatabase(value: ResultType?) {
    }

    protected abstract fun processResponse(apiResponse: ApiResponse<RequestType>): ResultType?
    private fun setValue(resource: ResourceWrapper<ResultType?>) {
        resultData.value = resource
    }

    open fun needFetchData(resultType: ResultType?) = false
    open fun loadFromDB(): LiveData<ResultType> {
        return MutableLiveData()
    }

    open fun isLoadFromDb(): Boolean {
        return false
    }

    @MainThread
    protected abstract fun getCallService(): LiveData<ApiResponse<RequestType>>

    fun getLiveData(): LiveData<ResourceWrapper<ResultType?>> {
        return resultData
    }

}