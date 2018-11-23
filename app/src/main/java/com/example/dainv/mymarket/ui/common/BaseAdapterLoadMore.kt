package com.example.dainv.mymarket.ui.common

import android.arch.lifecycle.MutableLiveData
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList

abstract class BaseAdapterLoadMore<I, V : ViewDataBinding> : BaseRecyclerViewAdapter<I, V>() {
//    private val items = ArrayList<I>()
//    private val itemClick = MutableLiveData<I>()
    // default isLastPage = true -> no have item load more
    protected var isLastPage: Boolean = true
    val loadMoreLiveData = MutableLiveData<Any>()
    override fun getItemCount(): Int {
        return if (isLastPage) {
            items.size
        } else {
            items.size + 1
        }
    }
    fun addItems(listItem: List<I>) {
        if (listItem == null) {
            return
        }
        items.addAll(listItem)
        notifyItemRangeChanged(itemCount - listItem.size - 1, itemCount)
    }

    fun setIsLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }
}