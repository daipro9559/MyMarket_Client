package com.example.dainv.mymarket.ui.items

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_items.*
import javax.inject.Inject

class ListItemActivity :BaseActivity(){

    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        initView()
    }
    private fun initView(){
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = itemAdapter.get()
    }
}