package com.example.dainv.mymarket.ui.dialog

import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemSelectDialogBinding
import com.example.dainv.mymarket.entity.District
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import javax.inject.Inject

class AdapterSelectDistrict @Inject constructor(): BaseRecyclerViewAdapter<District, ItemSelectDialogBinding>(){

    override fun getLayoutID()  = R.layout.item_select_dialog
    override fun bindData(p0: ItemViewHolder<ItemSelectDialogBinding>,position:Int) {
        p0.getViewBinding().title.text = items[position].districtName
    }
}