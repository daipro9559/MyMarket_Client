package com.example.dainv.mymarket.viewcustom

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class CategoryTarget<T> : Target<T>{
    override fun onLoadStarted(placeholder: Drawable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRequest(): Request? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRequest(request: Request?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCallback(cb: SizeReadyCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResourceReady(resource: T, transition: Transition<in T>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSize(cb: SizeReadyCallback) {
        cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

}