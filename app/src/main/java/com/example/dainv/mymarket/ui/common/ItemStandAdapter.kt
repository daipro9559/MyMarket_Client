package com.example.dainv.mymarket.ui.common

import android.support.v7.util.DiffUtil
import com.example.dainv.mymarket.AppExecutors
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemLayoutBinding
import com.example.dainv.mymarket.databinding.ItemStandBinding
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.model.Stand
import com.example.dainv.mymarket.util.Util
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ItemStandAdapter
@Inject constructor(appExecutors: AppExecutors) : BaseAsyncDiffItemAdapter<Stand, ItemStandBinding>(appExecutors, object : DiffUtil.ItemCallback<Stand>() {
    override fun areContentsTheSame(p0: Stand, p1: Stand): Boolean {
        return p0.standID == p1.standID
    }

    override fun areItemsTheSame(p0: Stand, p1: Stand): Boolean {
        return p0.standID == p1.standID
    }

}) {
    val itemMarkObserve = PublishSubject.create<String>()
    val itemUnMarkObserve = PublishSubject.create<String>()

    override fun bindView(p0: ItemViewHolder<ItemStandBinding>, i: Stand) {
        p0.getViewBinding().stand = i
    }

    override fun getLayoutID(): Int {
        return R.layout.item_stand
    }


}