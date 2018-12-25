package com.example.dainv.mymarket.ui.main.stands

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseFragment
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.common.ItemStandAdapter
import com.example.dainv.mymarket.ui.stand.detail.StandDetailActivity
import dagger.Lazy
import kotlinx.android.synthetic.main.app_bar_layout.view.*
import kotlinx.android.synthetic.main.fragment_stands.*
import javax.inject.Inject

class StandsFragment : BaseFragment(){
    @Inject
    lateinit var itemStandAdapter : Lazy<ItemStandAdapter>
    lateinit var standsViewModel: StandsViewModel
    companion object {
        val TAG = "stands fragment"
        fun newInstance(): StandsFragment {
            val standsFragment = StandsFragment()
            return standsFragment
        }
    }
    override fun getLayoutID() = R.layout.fragment_stands

    private var isLoadMore = false
    private var currentPage = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appBarLayout.toolBar.setTitle(R.string.list_stand)
        standsViewModel = ViewModelProviders.of(this,viewModelFactory)[StandsViewModel::class.java]

        recycleView.layoutManager = GridLayoutManager(context,2)
        recycleView.adapter = itemStandAdapter.get()
//        recycleView.addItemDecoration(DividerItemDecoration(context, (recycleView.layoutManager as LinearLayoutManager).orientation))
        standsViewModel.listStandLiveData.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING){
                loadingLayout.visibility = View.VISIBLE
            }else{
                loadingLayout.visibility = View.GONE
            }
            it!!.r?.let {it->
                itemStandAdapter.get().setIsLastPage(it.lastPage)
                if (isLoadMore) {
                    itemStandAdapter.get().addItems(it.data)
                    isLoadMore = false
                } else {
                    itemStandAdapter.get().swapItems(it.data)
                }
            }
        })
        itemStandAdapter.get().loadMoreLiveData.observe(this, Observer {
            standsViewModel.getStands(it!!)
        })
        itemStandAdapter.get().itemClickObserve().observe(this, Observer {
            val intent = Intent(activity,StandDetailActivity::class.java)
            intent.putExtra(StandDetailActivity.STAND_KEY,it)
            intent.putExtra(StandDetailActivity.IS_MY_STAND,false)
            startActivity(intent)
        })
    }
}