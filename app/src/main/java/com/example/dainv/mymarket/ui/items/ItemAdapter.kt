package com.example.dainv.mymarket.ui.items

import android.support.v7.util.DiffUtil
import android.widget.BaseAdapter
import com.example.dainv.mymarket.AppExecutors
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemLayoutBinding
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.service.response.ItemResponse
import com.example.dainv.mymarket.ui.common.BaseAsyncDiffItemAdapter
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class ItemAdapter
@Inject constructor(appExecutors: AppExecutors) : BaseAsyncDiffItemAdapter<Item,ItemLayoutBinding>(appExecutors,object :DiffUtil.ItemCallback<Item>(){
    override fun areContentsTheSame(p0: Item, p1: Item): Boolean {
        return p0.itemID==p1.itemID
    }

    override fun areItemsTheSame(p0: Item, p1: Item): Boolean {
        return p0.itemID==p1.itemID
    }

}){
    override fun bindView(p0: ItemViewHolder<ItemLayoutBinding>, i: Item) {

        p0.getViewBinding().txtPrice.text = Util.convertPriceToFormat(i.price)
        p0.getViewBinding().item = i
        p0.getViewBinding().cardView.setOnClickListener {
            itemClickObserve.value= i
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.item_layout
    }

}