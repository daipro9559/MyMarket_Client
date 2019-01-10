package com.example.dainv.mymarket.ui.marked

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemLayoutBinding
import com.example.dainv.mymarket.entity.Item
import com.example.dainv.mymarket.ui.common.BaseAdapterLoadMore
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ItemAdapter
@Inject constructor() : BaseAdapterLoadMore<Item, ItemLayoutBinding>() {
    companion object {
        const val TYPE_LIST_NORMAL = 0
        const val TYPE_LIST_MARKED = 1
    }

    var type: Int = TYPE_LIST_NORMAL
    var isMyItems = false
    override fun getLayoutID() = R.layout.item_layout
    val itemMarkObserve = PublishSubject.create<String>()
    val itemUnMarkObserve = PublishSubject.create<String>()
    val editClickObserve = PublishSubject.create<String>()
    override fun bindData(p0: ItemViewHolder<ItemLayoutBinding>, position: Int) {
        val context = p0.getViewBinding().rootViewContent.context
        val i = items[position]
        p0.getViewBinding().txtTime.text = Util.convertTime(i.updatedAt, context)
        if (i.isDone) {
            p0.getViewBinding().txtNeedToSell.setBackgroundResource(R.drawable.bg_txt_is_done)
            if (i.needToSell) {
                p0.getViewBinding().txtNeedToSell.text = context.getString(R.string.sold)
            } else {
                p0.getViewBinding().txtNeedToSell.text = context.getString(R.string.bought)
            }
        } else {
            if (i.needToSell) {
                p0.getViewBinding().txtNeedToSell.setBackgroundResource(R.drawable.bg_txt_need_to_sell)
                p0.getViewBinding().txtNeedToSell.text = context.getString(R.string.need_to_sale)

            } else {
                p0.getViewBinding().txtNeedToSell.setBackgroundResource(R.drawable.bg_txt_need_to_buy)
                p0.getViewBinding().txtNeedToSell.text = context.getString(R.string.need_to_buy)
            }
        }
        if (i.images != null && i.images.isNotEmpty()) {
            p0.getViewBinding().imagePath = i.images!![0]
        } else {
            p0.getViewBinding().imagePath = ""
        }
        p0.getViewBinding().txtPrice.text = Util.convertPriceToText(i.price, context)
        p0.getViewBinding().item = i
        p0.getViewBinding().isMyItems = isMyItems
        p0.getViewBinding().checkboxMark.setOnClickListener {
            if (p0.getViewBinding().checkboxMark.isChecked) {
                itemMarkObserve.onNext(i.itemID)
            } else {
                if (type == TYPE_LIST_MARKED) {
                    items.remove(i)
                    notifyItemRemoved(position)
                }
                itemUnMarkObserve.onNext(i.itemID)
            }

        }
        p0.getViewBinding().iconEdit.setOnClickListener {
            editClickObserve.onNext(i.itemID)
        }
    }


}