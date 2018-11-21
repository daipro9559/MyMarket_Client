package com.example.dainv.mymarket.base

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.glide.GlideApp

object BindViewStatic {
    @JvmStatic
    @BindingAdapter("bindImage")
    fun showImage(imageView: ImageView, url: String) {
        if (url.isNotEmpty()){

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
    fun bindImageLocal(imageView: ImageView, url: String) {
        GlideApp.with(imageView.context)
                .load(url)
                .into( DrawableImageViewTarget(imageView))
                .waitForLayout()
    }
}