package com.example.dainv.mymarket.util

import android.arch.lifecycle.LiveData
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean
import retrofit2.*
class LiveDataCallAdapter<R>(private val responseType : Type) : CallAdapter<R,LiveData<ApiResponse<R?>>>{
    override fun adapt(call: Call<R>?): LiveData<ApiResponse<R?>> {
        return object : LiveData<ApiResponse<R?>>(){
            private val started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false,true)){
                    call?.enqueue(object : Callback<R>{
                        override fun onFailure(call: Call<R>?, t: Throwable?) {
                            postValue(ApiResponse.createErrorResponse(t!!,500))
                        }
                        override fun onResponse(call: Call<R>?, response: Response<R>?) {
                            if (response!!.code() <=300) {
                                postValue(ApiResponse.createSuccessResponse(response!!.body(), response.code()))
                            }else{
                                postValue(ApiResponse.createErrorResponse(Throwable(response!!.message()),response!!.code()))
                            }

                        }
                    })
                }
            }
        }
    }

    override fun responseType() = responseType

}