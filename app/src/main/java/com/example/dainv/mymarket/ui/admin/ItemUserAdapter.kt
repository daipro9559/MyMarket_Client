package com.example.dainv.mymarket.ui.admin

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemUserBinding
import com.example.dainv.mymarket.entity.User
import com.example.dainv.mymarket.ui.common.BaseAdapterLoadMore
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import javax.inject.Inject

class ItemUserAdapter
@Inject
constructor()
    : BaseAdapterLoadMore<User, ItemUserBinding>() {
    override fun getLayoutID() = R.layout.item_user

    override fun bindData(p0: ItemViewHolder<ItemUserBinding>, position: Int) {
        val user = items[position]
        p0.getViewBinding().user = user
        if(user.avatar !=null){
            p0.getViewBinding().avatar  = user.avatar

        }else{
            p0.getViewBinding().avatar  = ""
        }
        p0.getViewBinding().txtTime.text = Util.convertTime(user.createdAt,p0.getViewBinding().root.context)
    }
}