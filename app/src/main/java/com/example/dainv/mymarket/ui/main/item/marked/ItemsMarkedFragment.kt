package com.example.dainv.mymarket.ui.main.item.marked

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import com.example.dainv.mymarket.ui.items.ItemAdapter
import dagger.Lazy
import kotlinx.android.synthetic.main.app_bar_layout.view.*
import kotlinx.android.synthetic.main.fragment_items_marked.*
import javax.inject.Inject

class ItemsMarkedFragment : BaseFragment() {
    companion object {
        fun newInstance(): ItemsMarkedFragment {
            return ItemsMarkedFragment()
        }
    }

    lateinit var itemsMarkedViewModel: ItemsMarkedViewModel
    override fun getLayoutID() = R.layout.fragment_items_marked
    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    private var currentPage =0
    private var isLoadMore = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        itemsMarkedViewModel = ViewModelProviders.of(this, viewModelFactory)[ItemsMarkedViewModel::class.java]
        initView()
        toolbarLayout.toolBar.setTitle(R.string.item_marked)
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
                    itemAdapter.get().addItems(it.data)
                }else {
                    itemAdapter.get().swapItems(it.data)
                }
            }
        })
        itemAdapter.get().loadMoreLiveData.observe(this, Observer {
            currentPage++
            isLoadMore = true
            itemsMarkedViewModel.getItemsMarked(currentPage)
        })
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = itemAdapter.get()
        itemAdapter.get().itemClickObserve().observe(this, Observer {
            val intent = Intent(activity, ItemDetailActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        })
    }
}