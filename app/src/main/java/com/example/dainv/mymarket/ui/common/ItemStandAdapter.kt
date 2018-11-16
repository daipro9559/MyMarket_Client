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
@Inject constructor() : BaseRecyclerViewAdapter<Stand, ItemStandBinding>(){

    val itemMarkObserve = PublishSubject.create<String>()
    val itemUnMarkObserve = PublishSubject.create<String>()

    override fun bindData(p0: ItemViewHolder<ItemStandBinding>, position: Int) {
        p0.getViewBinding().stand = getItems()[position]
    }

    override fun getLayoutID(): Int {
        return R.layout.item_stand
    }


}