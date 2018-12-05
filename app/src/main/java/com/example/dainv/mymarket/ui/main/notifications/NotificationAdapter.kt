package com.example.dainv.mymarket.ui.main.notifications

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemNotificationBinding
import com.example.dainv.mymarket.model.Notification
import com.example.dainv.mymarket.ui.common.BaseAdapterLoadMore
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import javax.inject.Inject

class NotificationAdapter
    @Inject constructor()
    : BaseAdapterLoadMore<Notification, ItemNotificationBinding>() {
    override fun getLayoutID() = R.layout.item_notification

    override fun bindData(p0: ItemViewHolder<ItemNotificationBinding>, position: Int) {
        val notification = items[position]
        p0.getViewBinding().notification = items[position]
        p0.getViewBinding().txtTime.text = Util.convertTime(items[position].updatedAt,p0.getViewBinding().root.context)
        if (notification.type == 3){

        }
    }
}