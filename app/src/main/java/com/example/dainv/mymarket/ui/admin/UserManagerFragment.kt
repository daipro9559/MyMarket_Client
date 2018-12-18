package com.example.dainv.mymarket.ui.admin

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_user_manager.*
import javax.inject.Inject

class UserManagerFragment : BaseFragment(){
    companion object {
        fun newInstance():UserManagerFragment{
            return UserManagerFragment()
        }
    }
    private lateinit var adminViewModel: AdminViewModel
    @Inject
    lateinit var userAdapter: ItemUserAdapter
    private var currentPage = 0
    private var isLoadMore = false
    override fun getLayoutID() = R.layout.fragment_user_manager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adminViewModel = ViewModelProviders.of(activity!!,viewModelFactory)[AdminViewModel::class.java]
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        viewObserve()
        adminViewModel.getUsers(currentPage)

    }
    private fun viewObserve(){
        userAdapter.loadMoreLiveData.observe(this, Observer {
            isLoadMore = true
            currentPage = it!!
            adminViewModel.getUsers(currentPage)
        })
        adminViewModel.listUserResult.observe(this, Observer {
            if (!isLoadMore) {
                viewLoading(it!!.resourceState, loadingLayout)
            }
            it!!.r?.let { it ->
                userAdapter.swapItems(it.data)
                userAdapter.setIsLastPage(it.lastPage)
                if (isLoadMore) {
                    userAdapter.addItems(it.data)
                    isLoadMore = false
                }
            }
        })
    }
    private fun initView(){
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = userAdapter
    }
}