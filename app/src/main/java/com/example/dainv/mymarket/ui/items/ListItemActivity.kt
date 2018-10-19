package com.example.dainv.mymarket.ui.items

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.ErrorResponse
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_items.*
import javax.inject.Inject

class ListItemActivity : BaseActivity() {
    lateinit var listItemViewModel: ListItemViewModel
    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    private val queryMap = HashMap<String,String>()
    private lateinit var categorySelect: Category
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        categorySelect = intent.getParcelableExtra("category")
        if (categorySelect!= null){
            queryMap["categoryID"] = categorySelect.categoryID.toString()
        }
        initView()
        listItemViewModel = ViewModelProviders.of(this, viewModelFactory)[ListItemViewModel::class.java]
        listItemViewModel.getItem(queryMap)
        viewObserve()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = itemAdapter.get()
    }

    private fun viewObserve() {
        itemAdapter.get().itemClickObserve.observe(this, Observer {
            val intent = Intent(this,ItemDetailActivity::class.java)
            intent.putExtra("item",it)
            startActivity(intent)
        })
        listItemViewModel.listItemLiveData.observe(this, Observer {
            it!!.r?.let {
                itemAdapter.get().submitList(it)
            }
        })
        listItemViewModel.errorLiveData.observe(this, Observer {
            if (it == ErrorResponse.UN_AUTHORIZED) {
                unAuthorize()
            }
        })
    }
}