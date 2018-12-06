package com.example.dainv.mymarket.base

import com.example.dainv.mymarket.model.ErrorResponse
import com.example.dainv.mymarket.util.ApiResponse
import com.example.dainv.mymarket.util.ErrorResponseLiveData
import com.example.dainv.mymarket.util.SharePreferencHelper
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

abstract class BaseRepository(protected val sharePreferencHelper: SharePreferencHelper) {
    var errorLiveData = ErrorResponseLiveData()
    protected var token: String?
        get() {
            if (field == null) {
                errorLiveData.value = ErrorResponse.UN_AUTHORIZED
            }
            return field
        }

    init {
        token = sharePreferencHelper.getString(Constant.TOKEN, null)
    }

    open fun hanlderCallService() {

    }

    open fun handlerErr(throwable: Throwable) {

    }

    protected fun <R> handlerResponse(response: ApiResponse<R>): R? {
        if (response.throwable != null) {
            when (response.throwable) {
                is TimeoutException -> {
                    errorLiveData.value = ErrorResponse.TIME_OUT
                }
                is IOException -> {
                    errorLiveData.value = ErrorResponse.NO_INTERNET
                }
                is UnknownHostException ->{
                    errorLiveData.value = ErrorResponse.UN_KNOWN
                }
                is HttpException ->{
                    val httpException = response.throwable as HttpException
                    if (httpException.code() == 401){
                        errorLiveData.value = ErrorResponse.UN_AUTHORIZED
                    }
                }
            }
            return null
        }
        return response.body
    }
}