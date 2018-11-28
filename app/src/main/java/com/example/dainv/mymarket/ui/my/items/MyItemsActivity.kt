package com.example.dainv.mymarket.ui.my.items

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.ui.marked.ItemAdapter
import com.example.dainv.mymarket.ui.items.ListItemViewModel
import com.example.dainv.mymarket.ui.items.RecycleViewSwipeHelper
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_items.*
import javax.inject.Inject

class MyItemsActivity : BaseActivity() {
    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    private lateinit var myItemsViewModel: MyItemsViewModel
    private var isUndoDelete = false
    private var positionDeleted: Int = -1
    private lateinit var itemWillDelete: Item
//    private var currentPage: Int = 0
    private var isLoadmore: Boolean = false
    private val queryMap = HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        setSupportActionBar(toolBar)
        enableHomeBack()
        title = getString(R.string.my_items_upload)
        myItemsViewModel = ViewModelProviders.of(this, viewModelFactory)[MyItemsViewModel::class.java]
        queryMap["isMyItems"] = true.toString()
        queryMap["page"] = 0.toString()
        myItemsViewModel.getItem(queryMap)
        initView()
        viewObserve()
    }
    private fun viewObserve(){
        itemAdapter.get().loadMoreLiveData.observe(this, Observer {
            isLoadmore = true
//            currentPage = it!!
            queryMap["page"] = it.toString()
            myItemsViewModel.getItem(queryMap)
        })
        myItemsViewModel.deleteResult.observe(this, Observer {
            it?.r?.let {success->
                if (success){
                    itemAdapter.get().items.removeAt(positionDeleted)
                }else{
                    Toast.makeText(applicationContext,R.string.can_not_delete, Toast.LENGTH_LONG).show()
                    itemAdapter.get().items.add(positionDeleted,itemWillDelete)
                    itemAdapter.get().notifyItemInserted(positionDeleted)
                }
            }
        })
        myItemsViewModel.listItemLiveData.observe(this, Observer {
            if (!isLoadmore) {
                viewLoading(it!!.resourceState, loadingLayout)
            }
            it!!.r?.let { it ->
                //                itemAdapter.get().submitList(it)
                itemAdapter.get().setIsLastPage(it.lastPage)
                if (isLoadmore) {
                    itemAdapter.get().addItems(it.data)
                    isLoadmore = false
                } else {
                    itemAdapter.get().swapItems(it.data)
                }
            }
        })
    }

    private fun initView() {
        cardViewIsNewest.visibility = View.GONE
        cardViewFree.visibility = View.GONE
        viewDivider1.visibility = View.GONE
        title = getString(R.string.my_items_upload)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        itemAdapter.get().isMyItems = true
        recyclerView.adapter = itemAdapter.get()
        val recycleViewSwipeHelper = RecycleViewSwipeHelper(applicationContext)
        val itemTouchHelper = ItemTouchHelper(recycleViewSwipeHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recycleViewSwipeHelper.onSwipedCompleted.subscribe {
            positionDeleted = it
            itemWillDelete = itemAdapter.get().items[positionDeleted]
            itemAdapter.get().items.removeAt(positionDeleted)
            itemAdapter.get().notifyItemRemoved(it)
            val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.deleting_item), Snackbar.LENGTH_LONG)
            snackbar.duration = 2000
            var isUndo = false
            snackbar.setAction(R.string.undo_delete) {
                itemAdapter.get().items.add(positionDeleted, itemWillDelete)
                itemAdapter.get().notifyItemInserted(positionDeleted)
                isUndo = true
                snackbar.dismiss()
            }
            snackbar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (!isUndo) {
                        myItemsViewModel.deleteItem(itemWillDelete.itemID)
                    }
                }
            })
            snackbar.show()
        }

    }
}