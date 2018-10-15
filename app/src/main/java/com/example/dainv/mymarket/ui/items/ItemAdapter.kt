package com.example.dainv.mymarket.ui.items

import android.support.v7.util.DiffUtil
import android.widget.BaseAdapter
import com.example.dainv.mymarket.AppExecutors
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemLayoutBinding
import com.example.dainv.mymarket.service.response.ItemResponse
import com.example.dainv.mymarket.ui.common.BaseAsyncDiffItemAdapter
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import javax.inject.Inject

class ItemAdapter
@Inject constructor(appExecutors: AppExecutors) : BaseAsyncDiffItemAdapter<ItemResponse,ItemLayoutBinding>(appExecutors,object :DiffUtil.ItemCallback<ItemResponse>(){
    override fun areContentsTheSame(p0: ItemResponse, p1: ItemResponse): Boolean {
        return false
    }

    override fun areItemsTheSame(p0: ItemResponse, p1: ItemResponse): Boolean {
        return false
    }

}){
    override fun bindView(p0: ItemViewHolder<ItemLayoutBinding>, i: ItemResponse) {

    }

    override fun getLayoutID(): Int {
        return R.layout.item_layout
    }

}