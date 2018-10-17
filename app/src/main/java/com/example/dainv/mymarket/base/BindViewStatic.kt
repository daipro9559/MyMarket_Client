package com.example.dainv.mymarket.base

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
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
    fun showImage(imageView: AppCompatImageView, url: String) {
        GlideApp.with(imageView.context)
                .load(Constant.BASE_URL+url)
                .placeholder(R.drawable.placeholder)
                .into( DrawableImageViewTarget(imageView))
    }
    @JvmStatic
    @BindingAdapter("bindImageLocal")
    fun bindImageLocal(imageView: AppCompatImageView, url: String) {
        GlideApp.with(imageView.context)
                .load(url)
                .into( DrawableImageViewTarget(imageView))
                .waitForLayout()
    }
}