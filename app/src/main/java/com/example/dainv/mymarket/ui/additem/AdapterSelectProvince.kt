package com.example.dainv.mymarket.ui.additem

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemSelectDialogBinding
import com.example.dainv.mymarket.model.Province
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import javax.inject.Inject

class AdapterSelectProvince @Inject constructor() : BaseRecyclerViewAdapter<Province,ItemSelectDialogBinding>(){
    override fun getLayoutID() = R.layout.item_select_dialog
    override fun bindData(p0: ItemViewHolder<ItemSelectDialogBinding>, item: Province) {
        p0.getViewBinding().title.text = item.provinceName
    }
}