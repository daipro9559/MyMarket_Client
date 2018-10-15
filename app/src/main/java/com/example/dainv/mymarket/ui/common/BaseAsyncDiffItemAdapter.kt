package com.example.dainv.mymarket.ui.common

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dainv.mymarket.AppExecutors

abstract class BaseAsyncDiffItemAdapter<I,V:ViewDataBinding>(appExecutor:AppExecutors
                                                             , diffCallback:DiffUtil.ItemCallback<I>)
    :ListAdapter<I,ItemViewHolder<V>>(AsyncDifferConfig.Builder<I>(diffCallback)
        .setBackgroundThreadExecutor(appExecutor.diskIO())
        .build()){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder<V> {
        val v  = DataBindingUtil.inflate<V>(LayoutInflater.from(p0.context),getLayoutID(),p0,false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(p0: ItemViewHolder<V>, p1: Int) {
        bindView(p0,getItem(p1))
        p0.getViewBinding().executePendingBindings()
    }
    abstract fun bindView(p0: ItemViewHolder<V>, i:I)
    public abstract fun getLayoutID():Int
}