package com.example.dainv.mymarket.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.example.dainv.mymarket.entity.ErrorResponse
import java.util.concurrent.atomic.AtomicBoolean

final class ErrorResponseLiveData : LiveData<ErrorResponse>(){
    private val changed = AtomicBoolean(false)
    public override fun setValue(value: ErrorResponse?) {
        changed.set(true)
        super.setValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ErrorResponse>) {
        super.observe(owner, Observer {
            if (changed.compareAndSet(true,false)){
                observer.onChanged(it)
            }
        })
    }
}