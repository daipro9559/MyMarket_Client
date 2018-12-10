package com.example.dainv.mymarket.ui.transaction

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemTransactionBinding
import com.example.dainv.mymarket.model.Transaction
import com.example.dainv.mymarket.ui.common.BaseAdapterLoadMore
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import javax.inject.Inject

class TransactionAdapter
    @Inject
    constructor()
    : BaseAdapterLoadMore<Transaction,ItemTransactionBinding>(){

    override fun getLayoutID() = R.layout.item_transaction

    override fun bindData(p0: ItemViewHolder<ItemTransactionBinding>, position: Int) {
        val context = p0.getViewBinding().rootViewContent.context
        val i = items[position]
        if (i.Item.images != null && i.Item.images.isNotEmpty()) {
            p0.getViewBinding().imageUrl =  i.Item.images!![0]
        } else {
            p0.getViewBinding().imageUrl = ""
        }
        p0.getViewBinding().txtTime.text = Util.convertTime(i.updatedAt, context)
        p0.getViewBinding().txtPrice.text = Util.convertPriceToText(i.price, context)
        p0.getViewBinding().txtName.text = i.Item.name
    }

}