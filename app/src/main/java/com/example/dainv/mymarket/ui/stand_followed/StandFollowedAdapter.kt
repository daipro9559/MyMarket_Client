package com.example.dainv.mymarket.ui.stand_followed

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemStandFollowedBinding
import com.example.dainv.mymarket.entity.Stand
import com.example.dainv.mymarket.ui.common.BaseAdapterLoadMore
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class StandFollowedAdapter
    @Inject
    constructor()
    : BaseAdapterLoadMore<Stand,ItemStandFollowedBinding>() {

    val itemUnFollowedObserve = PublishSubject.create<String>()

    override fun getLayoutID(): Int {
        return R.layout.item_stand_followed
    }

    override fun bindData(p0: ItemViewHolder<ItemStandFollowedBinding>, position: Int) {
        val stand = items[position]
        if(stand.image !=null && stand.image.isNotEmpty()){
            p0.getViewBinding().imagePath = stand.image[0]
        }
        p0.getViewBinding().stand = stand
        p0.getViewBinding().txtAddress.text = stand.Address!!.address + ", "+  stand.Address!!.District?.districtName+", "+stand.Address!!.District?.Province?.provinceName
        p0.getViewBinding().btnUnFollowed.setOnClickListener {
            itemUnFollowedObserve.onNext(stand.standID)
        }
    }
}