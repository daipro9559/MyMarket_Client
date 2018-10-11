package com.example.dainv.mymarket.base

import android.databinding.BindingAdapter
import android.support.v7.widget.AppCompatImageView
import com.bumptech.glide.Glide

object BindViewStatic {
    @JvmStatic
    @BindingAdapter("bindImage")
    fun showImage(imageView: AppCompatImageView, url: String) {
        Glide.with(imageView.context).load(Constant.BASE_URL+url).into(imageView)
    }
}