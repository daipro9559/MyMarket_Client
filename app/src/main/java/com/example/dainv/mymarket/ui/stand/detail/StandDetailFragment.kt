package com.example.dainv.mymarket.ui.stand.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.model.Stand
import com.example.dainv.mymarket.ui.items.ItemAdapter
import com.example.dainv.mymarket.ui.main.profile.ProfileFragment
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_stand_detail.*
import javax.inject.Inject

class StandDetailFragment :BaseFragment() {

    companion object {
        fun newInstance(): StandDetailFragment {
            val standDetailFragment = StandDetailFragment()
            return standDetailFragment
        }
    }
    private var stand: Stand? = null
    private var isMystand: Boolean = false
    lateinit var standDetailViewModel: StandDetailViewModel
    @Inject
    lateinit var itemAdapter: Lazy<ItemAdapter>
    private var currentPage = 0
    private var isLoadMore = false
    override fun getLayoutID() = R.layout.fragment_stand_detail
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromItem()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        standDetailViewModel = ViewModelProviders.of(this, viewModelFactory)[StandDetailViewModel::class.java]
        initView()
        itemAdapter.get().loadMoreLiveData.observe(this, Observer {
            isLoadMore = true
            currentPage++
            fetchItems()
        })
        standDetailViewModel.listItemLiveData.observe(this, Observer {

            if (!isLoadMore) {
                if (it!!.resourceState == ResourceState.LOADING){
                    loadingLayout.visibility = View.VISIBLE
                }else{
                    loadingLayout.visibility = View.GONE

                }
            }
            it!!.r?.let { it ->
                itemAdapter.get().setIsLastPage(it.lastPage)
                if (isLoadMore) {
                    itemAdapter.get().addItems(it.data)
                    isLoadMore = false
                } else {
                    itemAdapter.get().swapItems(it.data)
                }
                if(itemAdapter.get().getItems().isEmpty()){
                    layoutNotHave.visibility = View.VISIBLE
                }else{
                    layoutNotHave.visibility = View.GONE

                }
            }
        })
        fetchItems()
    }

    private fun initView(){
        recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = itemAdapter.get()
        itemAdapter.get().isMyItems = isMystand
    }
    private fun getDataFromItem() {
        stand = activity!!.intent.getParcelableExtra(StandDetailActivity.STAND_KEY)
        isMystand = activity!!.intent.getBooleanExtra(StandDetailActivity.IS_MY_STAND, false)
    }
        private fun fetchItems() {
        val queryMap = HashMap<String, String>()
        queryMap["standID"] = stand?.standID.toString()
        queryMap["page"] = currentPage.toString()
        if (isMystand){
            queryMap["isMyItems"] = true.toString()
        }
        standDetailViewModel.getItem(queryMap)
    }
}