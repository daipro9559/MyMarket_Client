package com.example.dainv.mymarket.ui.common

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemStandBinding
import com.example.dainv.mymarket.entity.Stand
import javax.inject.Inject

class ItemStandAdapter
@Inject constructor() : BaseAdapterLoadMore<Stand, ItemStandBinding>() {

    override fun bindData(p0: ItemViewHolder<ItemStandBinding>, position: Int) {
        p0.getViewBinding().stand = items[position]
    }

    override fun getLayoutID(): Int {
        return R.layout.item_stand
    }

}