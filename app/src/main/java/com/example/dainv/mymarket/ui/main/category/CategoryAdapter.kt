package com.example.dainv.mymarket.ui.main.category

import android.support.v7.util.DiffUtil
import com.example.dainv.mymarket.AppExecutors
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.databinding.ItemCategoryBinding
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.ui.common.BaseAdapter
import com.example.dainv.mymarket.ui.common.ItemViewHolder
import javax.inject.Inject
import kotlinx.android.synthetic.main.item_category.*

class CategoryAdapter
    @Inject constructor(
         appExecutors: AppExecutors
    )
    : BaseAdapter<Category,ItemCategoryBinding>(appExecutors,object: DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(p0: Category, p1: Category): Boolean {
        return p0.categoryID==p1.categoryID
    }

    override fun areContentsTheSame(p0: Category, p1: Category): Boolean {
        return p0.categoryID==p1.categoryID
    }
}){
    override fun bindView(p0: ItemViewHolder<ItemCategoryBinding>, i: Category) {
        p0.getViewBinding().category = i
    }

    init {

    }
    override fun getLayoutID() = R.layout.item_category


}