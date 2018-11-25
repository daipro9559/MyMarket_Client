package com.example.dainv.mymarket.ui.my.items

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.ui.marked.ItemAdapter
import com.example.dainv.mymarket.ui.items.ListItemViewModel
import dagger.Lazy
import javax.inject.Inject

class MyItemsActivity :BaseActivity() {
    @Inject
    lateinit var itemAdapter :Lazy<ItemAdapter>
    private lateinit var listItemViewModel : ListItemViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        listItemViewModel = ViewModelProviders.of(this,viewModelFactory)[ListItemViewModel::class.java]
    }
}