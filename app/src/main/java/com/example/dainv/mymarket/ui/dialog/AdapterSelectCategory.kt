package com.example.dainv.mymarket.ui.dialog

import android.view.ViewGroup
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemSelectDialogBinding
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.ui.common.BaseRecyclerViewAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import com.example.dainv.mymarket.util.ClickCallback
import javax.inject.Inject

class AdapterSelectCategory @Inject constructor(): BaseRecyclerViewAdapter<Category, ItemSelectDialogBinding>(){

    override fun getLayoutID()  = R.layout.item_select_dialog
    override fun bindData(p0: ItemViewHolder<ItemSelectDialogBinding>, position: Int) {
        p0.getViewBinding().title.text = getItems()[position].categoryName
    }
}