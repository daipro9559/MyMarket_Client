package com.example.dainv.mymarket.base

import android.arch.lifecycle.LiveData
import com.example.dainv.mymarket.model.ErrorResponse
import com.example.dainv.mymarket.util.ErrorResponseLiveData
import com.example.dainv.mymarket.util.SharePreferencHelper

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
}