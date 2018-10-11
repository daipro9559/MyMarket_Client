package com.example.dainv.mymarket.view.common

import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dainv.mymarket.AppExecutors

abstract class BaseAdapter<I>(val appExecutor:AppExecutors
,val diffCallback:DiffUtil.ItemCallback<I>)
    :ListAdapter<I,ItemViewHolder>(AsyncDifferConfig.Builder<I>(diffCallback)
        .setBackgroundThreadExecutor(appExecutor.diskIO())
        .build()){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(getLayoutID(),p0,false)
        return ItemViewHolder(view)
    }
    public abstract fun getLayoutID():Int
}