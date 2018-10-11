package com.example.dainv.mymarket.view.main.category

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.R
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_category.*
import javax.inject.Inject

class CategoryFragment : BaseFragment(){
    @Inject
    lateinit var categoryAdapter:  Lazy<CategoryAdapter>
    companion object {
        fun newInstance() : CategoryFragment{
            val categoryFragment = CategoryFragment()
            val bundle = Bundle()
            categoryFragment.arguments = bundle
            return categoryFragment
        }
    }

    private lateinit var categoryViewModel:CategoryViewModel
    override fun getLayoutID() = R.layout.fragment_category
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this,viewModelFactory)[CategoryViewModel::class.java]
        recyclerView.layoutManager = LinearLayoutManager(context!!.applicationContext,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = categoryAdapter.get()
        categoryViewModel.listCategory.observe(this, Observer {
            if (it!!.r!=null){
                categoryAdapter.get().submitList(it!!.r)
            }
        })
    }


}