package com.example.dainv.mymarket.view.main.category

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.R
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : BaseFragment(){
    private lateinit var categoryViewModel:CategoryViewModel
    override fun getLayoutID() = R.layout.fragment_category

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this,viewModelFactory)[CategoryViewModel::class.java]
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

    }

}