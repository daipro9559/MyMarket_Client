package com.example.dainv.mymarket.ui.main.item.marked

import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemLayoutBinding
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ItemAdapter
@Inject constructor() : BaseRecyclerViewAdapter<Item, ItemLayoutBinding>() {
    companion object {
        val TYPE_LIST_NORMAL = 0
        val TYPE_LIST_MARKED = 1
    }

    var type: Int = TYPE_LIST_NORMAL
    var isMyItems = false
    override fun getLayoutID() = R.layout.item_layout
    val itemMarkObserve = PublishSubject.create<String>()
    val itemUnMarkObserve = PublishSubject.create<String>()
    override fun bindData(p0: ItemViewHolder<ItemLayoutBinding>, position: Int) {
        if (!isLastPage && p0.adapterPosition == itemCount - 1) {
            p0.getViewBinding().rootView.visibility = View.GONE
            p0.getViewBinding().loadMoreProgress.visibility = View.VISIBLE
            loadMoreLiveData.value = Any()
        } else {
            val context = p0.getViewBinding().rootView.context
            val i = getItems()[position]
            p0.getViewBinding().txtTime.text = Util.convertTime(i.updatedAt,context)
            if (i.isDone){
                p0.getViewBinding().txtNeedToSell.setBackgroundResource(R.drawable.bg_txt_is_done)
                if (i.needToSell){
                    p0.getViewBinding().txtNeedToSell.text = context.getString(R.string.sold)
                }else{
                    p0.getViewBinding().txtNeedToSell.text = context.getString(R.string.bought)
                }
            }else{
                if (i.needToSell){
                    p0.getViewBinding().txtNeedToSell.setBackgroundResource(R.drawable.bg_txt_need_to_sell)
                    p0.getViewBinding().txtNeedToSell.text = context.getString(R.string.need_to_sale)

                }else{
                    p0.getViewBinding().txtNeedToSell.setBackgroundResource(R.drawable.bg_txt_need_to_buy)
                    p0.getViewBinding().txtNeedToSell.text = context.getString(R.string.need_to_buy)
                }
            }
            p0.getViewBinding().rootView.visibility = View.VISIBLE
            p0.getViewBinding().loadMoreProgress.visibility = View.GONE
            if (i.images != null && i.images.isNotEmpty()) {
                p0.getViewBinding().imagePath = i.images!![0]
            } else {
                p0.getViewBinding().imagePath = ""
            }
            p0.getViewBinding().txtPrice.text = Util.convertPriceToText(i.price,context)
            p0.getViewBinding().item = i
            p0.getViewBinding().isMyItems = isMyItems
            p0.getViewBinding().checkboxMark.setOnClickListener {
                if (p0.getViewBinding().checkboxMark.isChecked) {
                    itemMarkObserve.onNext(i.itemID)
                } else {
                    if (type == TYPE_LIST_MARKED) {
                        getItems().remove(i)
                        notifyItemRemoved(position)
                    }
                    itemUnMarkObserve.onNext(i.itemID)
                }
            }
        }
    }


}