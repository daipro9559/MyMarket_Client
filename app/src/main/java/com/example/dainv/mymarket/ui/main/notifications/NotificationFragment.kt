package com.example.dainv.mymarket.ui.main.notifications

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.model.ResourceState
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_notification.*
import javax.inject.Inject

class NotificationFragment : BaseFragment() {
    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }

    lateinit var notificationViewModel: NotificationViewModel
    @Inject
    lateinit var notificationAdapter: Lazy<NotificationAdapter>

    override fun getLayoutID() = R.layout.fragment_notification
    private var currentPage = 0
    private var isLoadMore = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notificationViewModel = ViewModelProviders.of(this, viewModelFactory)[NotificationViewModel::class.java]
        initView()
        notificationViewModel.notificationsLiveData.observe(this, Observer { resourceWrapper ->
            if (resourceWrapper!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
            }
            resourceWrapper.r?.let {
                notificationAdapter.get().setIsLastPage(it.lastPage)
                if (isLoadMore) {
                    isLoadMore = false
                    it.data?.let { items ->
                        notificationAdapter.get().addItems(items)
                    }
                } else {
                    it.data?.let { items ->
                        notificationAdapter.get().swapItems(items)
                    }
                }
                if (notificationAdapter.get().items.isEmpty()) {
                    layoutNotHave.visibility = View.VISIBLE
                } else {
                    layoutNotHave.visibility = View.GONE

                }
            }
        })
    }

    private fun initView() {
        recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = notificationAdapter.get()
    }
}