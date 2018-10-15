package com.example.dainv.mymarket.ui.common

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.ArrayList

abstract class BaseRecyclerViewAdapter<I,V :ViewDataBinding> :RecyclerView.Adapter<ItemViewHolder<V>>() {
    private val items  = ArrayList<I>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder<V> {
        return ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(p0.context),
                getLayoutID(),
                p0,
                false))
    }

    protected abstract fun getLayoutID():Int

    override fun getItemCount() = items.size

    override fun onBindViewHolder(p0: ItemViewHolder<V>, p1: Int) {
        bindData(p0,p1)
        p0.getViewBinding().executePendingBindings()
    }
    protected abstract fun bindData(p0: ItemViewHolder<V>,position:Int)

    public fun addItem(i : I){
        items.add(i)
        notifyItemChanged(items.size -1)
    }

    public fun swapItems(listItem:List<I>){
        if(listItem ==null){
            items.clear()
            return
        }
        items.clear()
        items.addAll(listItem)
        notifyDataSetChanged()
    }

    public fun getItems() = items
}