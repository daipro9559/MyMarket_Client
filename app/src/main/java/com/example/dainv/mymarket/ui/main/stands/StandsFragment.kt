package com.example.dainv.mymarket.ui.main.stands

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.common.ItemStandAdapter
import com.example.dainv.mymarket.ui.main.profile.ProfileFragment
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appBarLayout.toolBar.setTitle(R.string.list_stand)
        standsViewModel = ViewModelProviders.of(this,viewModelFactory)[StandsViewModel::class.java]
        standsViewModel.getStands()
        recycleView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        recycleView.adapter = itemStandAdapter.get()
        recycleView.addItemDecoration(DividerItemDecoration(context, (recycleView.layoutManager as LinearLayoutManager).orientation))
        standsViewModel.listStandLiveData.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING){
                loadingLayout.visibility = View.VISIBLE
            }else{
                loadingLayout.visibility = View.GONE
            }
            it!!.r?.let {
                itemStandAdapter.get().submitList(it)
            }
        })
    }
}