package com.example.dainv.mymarket.ui.additem

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemSelectDialogBinding
import com.example.dainv.mymarket.model.Province
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import javax.inject.Inject

class AdapterSelectProvince @Inject constructor() : BaseRecyclerViewAdapter<Province,ItemSelectDialogBinding>(){
    override fun getLayoutID() = R.layout.item_select_dialog
    override fun bindData(p0: ItemViewHolder<ItemSelectDialogBinding>, position: Int) {
        p0.getViewBinding().title.text = getItems()[position].provinceName
    }
}