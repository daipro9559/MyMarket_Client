package com.example.dainv.mymarket.ui.items

import android.support.v7.util.DiffUtil
import android.view.View
import com.example.dainv.mymarket.AppExecutors
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemLayoutBinding
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.ui.common.BaseAsyncDiffItemAdapter
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ItemAdapter
@Inject constructor() : BaseRecyclerViewAdapter<Item, ItemLayoutBinding>() {
    override fun getLayoutID() = R.layout.item_layout

    override fun bindData(p0: ItemViewHolder<ItemLayoutBinding>, i: Item?) {
        if (!isLastPage && p0.adapterPosition == itemCount-1){
            p0.getViewBinding().rootView.visibility = View.GONE
            p0.getViewBinding().loadMoreProgress.visibility = View.VISIBLE
            loadMoreLiveData.value = Any()
        }else {
            p0.getViewBinding().rootView.visibility = View.VISIBLE
            p0.getViewBinding().loadMoreProgress.visibility = View.GONE
            if (i.images != null && i.images.isNotEmpty()) {
                p0.getViewBinding().imagePath = i.images!![0]
            } else {
                p0.getViewBinding().imagePath = ""
            }
            p0.getViewBinding().txtPrice.text = Util.convertPriceToFormat(i.price)
            p0.getViewBinding().item = i
            p0.getViewBinding().checkboxMark.setOnCheckedChangeListener { v, checked ->
                if (checked) {
                    itemMarkObserve.onNext(i.itemID)
                } else {
                    itemUnMarkObserve.onNext(i.itemID)
                }
            }
        }
    }


    val itemMarkObserve = PublishSubject.create<String>()
    val itemUnMarkObserve = PublishSubject.create<String>()
}