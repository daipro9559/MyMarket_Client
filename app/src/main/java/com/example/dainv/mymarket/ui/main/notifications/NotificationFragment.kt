package com.example.dainv.mymarket.ui.main.notifications

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment
import com.example.dainv.mymarket.model.Notification
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import com.example.dainv.mymarket.ui.items.RecycleViewSwipeHelper
import dagger.Lazy
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.fragment_notification.*
import org.json.JSONObject
import timber.log.Timber
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
    private var positionDeleted = -1
    private lateinit var notificationDeleted: Notification

    override fun getLayoutID() = R.layout.fragment_notification
    private var currentPage = 0
    private var isLoadMore = false
    private var indexConfirmRequest :Int = -1
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notificationViewModel = ViewModelProviders.of(this, viewModelFactory)[NotificationViewModel::class.java]
        initView()
        viewObserve()
    }

    private fun viewObserve() {
        notificationAdapter.get().loadMoreLiveData.observe(this, Observer {
            isLoadMore = true
            currentPage = it!!
            notificationViewModel.getNotification(currentPage)
        })

        notificationAdapter.get().confirmClick.subscribe {
            indexConfirmRequest=it
            notificationViewModel.confirmRequest(notificationAdapter.get().items[it])
        }
        notificationViewModel.confirmResult.observe(this, Observer {
            Timber.e(it!!.resourceState.toString())
            if (it!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
            }
            if (it!!.resourceState == ResourceState.SUCCESS){
                notificationAdapter.get().items.removeAt(indexConfirmRequest)
                notificationAdapter.get().notifyItemRemoved(indexConfirmRequest)
            }
        })
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
        notificationViewModel.deleteResultData.observe(this, Observer { resourceWrapper ->
            resourceWrapper?.r?.success?.let {
                if (it) {
                    Toast.makeText(context!!, R.string.delete_completed, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context!!, R.string.can_not_delete, Toast.LENGTH_LONG).show()
                    notificationAdapter.get().items.add(positionDeleted, notificationDeleted)
                    notificationAdapter.get().notifyItemInserted(positionDeleted)
                }

            }
        })
        notificationAdapter.get().itemClickObserve().observe(this, Observer {
            val jsonObject = JSONObject(it!!.data)
            val intentItemDetail = Intent(activity, ItemDetailActivity::class.java)
            intentItemDetail.putExtra("itemID", jsonObject.get("itemID").toString())
            intentItemDetail.action = ItemDetailActivity.ACTION_SHOW_FROM_ID
            startActivityWithAnimation(intentItemDetail)
        })
    }

    private fun initView() {
        toolBar.setTitle(R.string.my_notification)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val recycleViewSwipeHelper = RecycleViewSwipeHelper(context!!)
        val itemTouchHelper = ItemTouchHelper(recycleViewSwipeHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recycleViewSwipeHelper.onSwipedCompleted.subscribe {
            positionDeleted = it
            notificationDeleted = notificationAdapter.get().items[positionDeleted]
            notificationAdapter.get().items.removeAt(positionDeleted)
            notificationAdapter.get().notifyItemRemoved(positionDeleted)
            val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.deleting_notification), Snackbar.LENGTH_LONG)
            snackbar.duration = 2000
            var isUndo = false
            snackbar.setAction(R.string.undo_delete) {
                notificationAdapter.get().items.add(positionDeleted, notificationDeleted)
                notificationAdapter.get().notifyItemInserted(positionDeleted)
                isUndo = true
                snackbar.dismiss()
            }
            snackbar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (!isUndo) {
                        notificationViewModel.deleteNotification(notificationDeleted.notificationID)
                    }
                }
            })
            snackbar.show()
        }
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = notificationAdapter.get()
    }
}