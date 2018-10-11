package com.example.dainv.mymarket.view.common

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.*

class ItemViewHolder<V :ViewDataBinding>( private val  v : V) : RecyclerView.ViewHolder(v.root)
{
    fun getViewBinding() : V = v
}