package com.example.dainv.mymarket.constant

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.glide.GlideApp

object BindViewStatic {
    @JvmStatic
    @BindingAdapter("bindImage")
    fun showImage(imageView: ImageView, url: String?) {
        if (url !=null && url.isNotEmpty()){
            GlideApp.with(imageView.context)
                    .load(Constant.BASE_URL+url)
                    .placeholder(R.drawable.placeholder)
                    .into( DrawableImageViewTarget(imageView))
            return
        }
        GlideApp.with(imageView.context)
                .load(R.drawable.placeholder)
                .into( DrawableImageViewTarget(imageView))

    }
    @JvmStatic
    @BindingAdapter("bindImageLocal")
    fun bindImageLocal(imageView: ImageView, url: String?) {
        GlideApp.with(imageView.context)
                .load(url)
                .into( DrawableImageViewTarget(imageView))
                .waitForLayout()
    }
}