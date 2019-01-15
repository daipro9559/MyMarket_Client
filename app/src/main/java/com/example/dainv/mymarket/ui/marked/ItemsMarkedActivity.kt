package com.example.dainv.mymarket.ui.marked

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_items_marked.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import javax.inject.Inject

class ItemsMarkedActivity : BaseActivity() {
    companion object {
        fun newInstance(): ItemsMarkedActivity {
            return ItemsMarkedActivity()
        }
    }

    lateinit var itemsMarkedViewModel: ItemsMarkedViewModel
    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    private var isLoadMore = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_marked)
        setSupportActionBar(toolBar)
        enableHomeBack()
        itemsMarkedViewModel = ViewModelProviders.of(this, viewModelFactory)[ItemsMarkedViewModel::class.java]
        initView()
        setTitle(R.string.item_marked)
        itemsMarkedViewModel.listItemMarkedResult.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
            }
            it?.r?.let { it ->
                itemAdapter.get().setIsLastPage(it.lastPage)
                if (isLoadMore){
                    isLoadMore = false
                    it.data?.let {items->
                        itemAdapter.get().addItems(items)
                    }
                }else {
                    it.data?.let {items->
                        itemAdapter.get().swapItems(items)
                    }
                }
                if (itemAdapter.get().items.isEmpty()){
                    layoutNotHave.visibility = View.VISIBLE
                }else{
                    layoutNotHave.visibility = View.GONE

                }
            }
        })
        itemAdapter.get().loadMoreLiveData.observe(this, Observer {
            isLoadMore = true
            itemsMarkedViewModel.getItemsMarked(it!!)
        })
        itemsMarkedViewModel.itemUnmarkResult.observe(this, Observer {
            it?.r?.let {
                Toast.makeText(applicationContext!!.applicationContext,R.string.unmark_item_completed,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initView() {
        itemAdapter.get().type = ItemAdapter.TYPE_LIST_MARKED
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = itemAdapter.get()
        itemAdapter.get().itemClickObserve().observe(this, Observer {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("item_view_pager", it)
            startActivity(intent)
        })
        itemAdapter.get().itemUnMarkObserve.subscribe{
            itemsMarkedViewModel.unMarkItem(it)
        }
    }
}