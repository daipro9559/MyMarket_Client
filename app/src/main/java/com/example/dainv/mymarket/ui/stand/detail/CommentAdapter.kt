package com.example.dainv.mymarket.ui.stand.detail

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemCommentBinding
import com.example.dainv.mymarket.entity.Comment
import com.example.dainv.mymarket.ui.common.BaseAdapterLoadMore
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.Util
import javax.inject.Inject

class CommentAdapter
    @Inject
    constructor()
    : BaseAdapterLoadMore<Comment,ItemCommentBinding>() {
    override fun getLayoutID() = R.layout.item_comment

    override fun bindData(p0: ItemViewHolder<ItemCommentBinding>, position: Int) {
       val comment = items[position]
        p0.getViewBinding().comment =  comment
        if(comment.User.avatar !=null){
            p0.getViewBinding().avatar  = comment.User.avatar

        }else{
            p0.getViewBinding().avatar  = ""
        }
        p0.getViewBinding().txtTime.text = Util.convertTime(comment.updatedAt, p0.getViewBinding().rootView.context)

    }
}