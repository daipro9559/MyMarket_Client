package com.example.dainv.mymarket.ui.dialog

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseDialogSelect
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.additem.AddItemViewModel
import dagger.Lazy
import kotlinx.android.synthetic.main.dialog_select.*
import java.util.ArrayList
import javax.inject.Inject

class DialogSelectCategory : BaseDialogSelect<Category>() {
    companion object {
        const val TAG = "dialog select category"
        const val LIST_CATEGORY_KEY = "list category"
        fun newInstance(categories: List<Category>): DialogSelectCategory {
            val bundle = Bundle()
            val dialogSelectCategory = DialogSelectCategory()
            bundle.putParcelableArrayList(LIST_CATEGORY_KEY, categories as ArrayList<out Parcelable>)
            dialogSelectCategory.arguments = bundle
            return dialogSelectCategory
        }

        fun newInstance(): DialogSelectCategory {
            val bundle = Bundle()
            val dialogSelectCategory = DialogSelectCategory()
            dialogSelectCategory.arguments = bundle
            return dialogSelectCategory
        }
    }

    private lateinit var listCategory: List<Category>
    lateinit var addItemViewModel: AddItemViewModel
    @Inject
    lateinit var adapterSelectCategory: Lazy<AdapterSelectCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        listCategory = arguments!!.getParcelableArrayList(LIST_CATEGORY_KEY)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        titleDialog.text = getString(R.string.select_category)
         adapterSelectCategory.get().swapItems(arguments!!.getParcelableArrayList(LIST_CATEGORY_KEY))
    }

    override fun initView() {
        super.initView()
//        adapterSelectCategory.get().swapItems(listCategory)
        adapterSelectCategory.get().itemCLickObserve().subscribe {
            callback.invoke(it)
            dismiss()
        }
        recycleView.adapter = adapterSelectCategory.get()
    }
}