package com.example.dainv.mymarket.ui.common

import android.arch.lifecycle.MutableLiveData
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList

abstract class BaseRecyclerViewAdapter<I, V : ViewDataBinding> : RecyclerView.Adapter<ItemViewHolder<V>>() {
    private val items = ArrayList<I>()
    private val itemClick = MutableLiveData<I>()
    // default isLastPage = true -> no have item load more
    protected var isLastPage: Boolean = true
    val loadMoreLiveData = MutableLiveData<Any>()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder<V> {
        val v = ItemViewHolder(createViewBinding(p0))
        v.getViewBinding().root.setOnClickListener {
            itemClick.value = items[v.adapterPosition]
        }
        return v
    }

    open fun createViewBinding(p0: ViewGroup): V {
        return DataBindingUtil.inflate(LayoutInflater.from(p0.context),
                getLayoutID(),
                p0,
                false)
    }

    protected abstract fun getLayoutID(): Int

    override fun getItemCount(): Int {
        return if (isLastPage) {
            items.size
        } else {
            items.size + 1
        }
    }

    override fun onBindViewHolder(p0: ItemViewHolder<V>, p1: Int) {

        bindData(p0, p1)
        p0.getViewBinding().executePendingBindings()
    }

    protected abstract fun bindData(p0: ItemViewHolder<V>, position: Int)

    fun addItem(i: I) {
        items.add(i)
        notifyItemChanged(items.size - 1)
    }

    fun addItems(listItem: List<I>) {
        if (listItem == null) {
            return
        }
        items.addAll(listItem)
        notifyItemRangeChanged(itemCount - listItem.size - 1, itemCount)
    }

    public fun swapItems(listItem: List<I>) {
        if (listItem == null) {
            items.clear()
            return
        }
        items.clear()
        items.addAll(listItem)
        notifyDataSetChanged()
    }

    fun itemClickObserve() = itemClick

    fun getItems() = items

    fun setIsLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }
}