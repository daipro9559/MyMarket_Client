package com.example.dainv.mymarket.ui.common

import android.arch.lifecycle.MutableLiveData
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dainv.mymarket.R
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList

abstract class BaseAdapterLoadMore<I, V : ViewDataBinding> : BaseRecyclerViewAdapter<I, V>() {
    //    private val items = ArrayList<I>()
//    private val itemClick = MutableLiveData<I>()
    // default isLastPage = true -> no have item load more
    protected var isLastPage: Boolean = true
    val loadMoreLiveData = MutableLiveData<Int>()!!// observe page to view
    protected var currentPage = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder<V> {
        val v = ItemViewHolder(createViewBinding(p0))
        v.getViewBinding().root.setOnClickListener {
            if (!isLastPage && v.adapterPosition == itemCount - 1) {
                return@setOnClickListener
            }
            itemClick.value = items[v.adapterPosition]
        }
        return v
    }
    override fun onBindViewHolder(p0: ItemViewHolder<V>, p1: Int) {
        if (!isLastPage && p0.adapterPosition == itemCount - 1) {
            p0.getViewBinding().root.findViewById<View>(R.id.rootViewContent).visibility = View.GONE
            p0.getViewBinding().root.findViewById<View>(R.id.loadMoreProgress).visibility = View.VISIBLE
            currentPage++
            loadMoreLiveData.value = currentPage
        } else {
            p0.getViewBinding().root.findViewById<View>(R.id.rootViewContent).visibility = View.VISIBLE
            p0.getViewBinding().root.findViewById<View>(R.id.loadMoreProgress).visibility = View.GONE
            bindData(p0, p1)
            p0.getViewBinding().executePendingBindings()
        }
    }

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