package com.example.dainv.mymarket.ui.items

import android.support.v7.util.DiffUtil
import com.example.dainv.mymarket.AppExecutors
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemLayoutBinding
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.ui.common.BaseAsyncDiffItemAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ItemAdapter
@Inject constructor(appExecutors: AppExecutors) : BaseAsyncDiffItemAdapter<Item, ItemLayoutBinding>(appExecutors, object : DiffUtil.ItemCallback<Item>() {
    override fun areContentsTheSame(p0: Item, p1: Item): Boolean {
        return p0.itemID == p1.itemID
    }

    override fun areItemsTheSame(p0: Item, p1: Item): Boolean {
        return p0.itemID == p1.itemID
    }

}) {
    val itemMarkObserve = PublishSubject.create<String>()
    val itemUnMarkObserve = PublishSubject.create<String>()
    override fun bindView(p0: ItemViewHolder<ItemLayoutBinding>, i: Item) {
        if (i.images != null && i.images.isNotEmpty()) {
            p0.getViewBinding().imagePath = i.images!![0]
        } else {
            p0.getViewBinding().imagePath = ""
        }
        p0.getViewBinding().txtPrice.text = Util.convertPriceToFormat(i.price)
        p0.getViewBinding().item = i
        p0.getViewBinding().cardView.setOnClickListener {
            itemClickObserve.value = i
        }
        p0.getViewBinding().checkboxMark.setOnCheckedChangeListener { v, checked ->
            if (checked) {
                itemMarkObserve.onNext(i.itemID)
            } else {
                itemUnMarkObserve.onNext(i.itemID)
            }
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.item_layout
    }

}