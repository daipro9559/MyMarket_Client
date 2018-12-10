package com.example.dainv.mymarket.ui.transaction

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import javax.inject.Inject

class TransactionActivity : BaseActivity(){
    companion object {
        const val ACTION_ADD_ITEM_FOR_STAND = "add_item_for_stand"
    }
    @Inject
    lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionViewModel: TransactionViewModel
    private var currentPage = 0
    private var isLoadMore = false
    private val mapQuery  = HashMap<String,String>()
    private var isAddToStand = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        setSupportActionBar(toolBar)
        enableHomeBack()
        setTitle(R.string.item_my_transaction)
        transactionViewModel = ViewModelProviders.of(this,viewModelFactory)[TransactionViewModel::class.java]
        initView()
        viewObserve()
        if (intent.action != null && intent.action == ACTION_ADD_ITEM_FOR_STAND){
            isAddToStand = true
            mapQuery["addToStand"] = true.toString()
            setTitle(R.string.add_item_to_stand)
            add_to_stand_title.visibility = View.VISIBLE
         }else{
            setTitle(R.string.item_my_transaction)
        }
        mapQuery["page"] = currentPage.toString()
        transactionViewModel.getTransaction(mapQuery)
    }
    private fun viewObserve(){
        transactionAdapter.itemClickObserve().observe(this, Observer {
            if (isAddToStand){
               val standID =  intent.getStringExtra(AddItemActivity.STAND_KEY)
                transactionViewModel.addItemToStand(it!!.Item.itemID,standID)
            }
        })
        transactionViewModel.addItemToStandResult.observe(this, Observer {resourceWrapper->
            if (resourceWrapper!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
            }
            if (resourceWrapper.resourceState == ResourceState.SUCCESS){
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
        transactionAdapter.loadMoreLiveData.observe(this, Observer {
            isLoadMore = true
            currentPage = it!!
            mapQuery["page"] = currentPage.toString()
            transactionViewModel.getTransaction(mapQuery)
        })
        transactionViewModel.listTransactionLiveData.observe(this, Observer {resourceWrapper->
            if (resourceWrapper!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
            }
            resourceWrapper.r?.let {
                transactionAdapter.setIsLastPage(it.lastPage)
                if (isLoadMore) {
                    isLoadMore = false
                    it.data?.let { items ->
                        transactionAdapter.addItems(items)
                    }
                } else {
                    it.data?.let { items ->
                        transactionAdapter.swapItems(items)
                    }
                }
                if (transactionAdapter.items.isEmpty()) {
                    layoutNotHave.visibility = View.VISIBLE
                } else {
                    layoutNotHave.visibility = View.GONE
                }
            }
        })
    }

    private fun initView(){
        recycleView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        recycleView.adapter = transactionAdapter

    }
}