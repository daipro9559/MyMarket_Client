package com.example.dainv.mymarket.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

final class ErrorResponseLiveData : LiveData<Error>(){
    val changed = AtomicBoolean(false)
    override fun setValue(value: Error?) {
        changed.set(true)
        super.setValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<Error>) {
        super.observe(owner, Observer {
            if (changed.compareAndSet(true,false)){
                observer.onChanged(it)
            }
        })
    }
}