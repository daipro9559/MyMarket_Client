package com.example.dainv.mymarket.ui.itemdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.databinding.ActivityItemDetailBinding
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.common.ViewPagerAdapter
import com.example.dainv.mymarket.ui.items.ListItemViewModel
import com.example.dainv.mymarket.util.SharePreferencHelper
import com.example.dainv.mymarket.util.Util
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import javax.inject.Inject

class ItemDetailActivity : BaseActivity() {
    @Inject
    lateinit var sharePreferencHelper: SharePreferencHelper

    companion object {
        const val ACTION_SHOW_FROM_ID = "show_from_notification"
        const val ACTION_CREATE_ITEM_COMPLETED = "create item completed"
        const val ACTION_NORMAL = "action_mormal"
    }

    private lateinit var item: Item
    private var viewBinding: ActivityItemDetailBinding? = null

    private lateinit var itemDetailViewModel: ItemDetailViewModel
    private lateinit var listItemViewModel: ListItemViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var action: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        toolBar.setTitle(R.string.detail_item_activity_title)
        setSupportActionBar(toolBar)
        enableHomeBack()
        itemDetailViewModel = ViewModelProviders.of(this, viewModelFactory)[ItemDetailViewModel::class.java]
        listItemViewModel = ViewModelProviders.of(this, viewModelFactory)[ListItemViewModel::class.java]
        getDataFromIntent()
        itemDetailViewModel.phoneDataLiveData.observe(this, Observer {
            if (it!!.resourceState == ResourceState.SUCCESS) {
                val phone = it.r
                val bottomDialogSelectContact = BottomDialogSelectContact.newsInstance()
                bottomDialogSelectContact.clickCallback = {
                    val intent = Intent()
                    if (it == R.id.actionOne) {
                        intent.action = Intent.ACTION_DIAL
                        val data = "tel:$phone"
                        intent.data = Uri.parse(data)

                    } else {//action message
                        intent.action = Intent.ACTION_SENDTO
                        val data = "smsto:$phone"
                        intent.data = Uri.parse(data)
                    }
                    startActivity(intent)
                    bottomDialogSelectContact.dismiss()
                }
                supportFragmentManager.beginTransaction().add(bottomDialogSelectContact, "").disallowAddToBackStack().commit()
            }
        })
        itemDetailViewModel.itemDetailResult.observe(this, Observer {
            it?.r?.let { result ->
                item = result
                showItem()
            }
        })

        checkboxMark.setOnClickListener {
            if (checkboxMark.isChecked) {
                listItemViewModel.markItem(item.itemID)
            } else {
                listItemViewModel.unMarkItem(item.itemID)
            }
        }
        listItemViewModel.itemMarkResult.observe(this, Observer {
            it?.r?.let {
                if (it) {
                    Toast.makeText(applicationContext, getString(R.string.mark_item_completed), Toast.LENGTH_LONG).show()
                }
            }
        })
        listItemViewModel.itemUnmarkResult.observe(this, Observer {
            it?.r?.let { it ->
                if (it) {
                    Toast.makeText(applicationContext, getString(R.string.unmark_item_completed), Toast.LENGTH_LONG).show()
                }
            }
        })
        btnRequestConfirm.setOnClickListener {
            itemDetailViewModel.requestBuyItem(item!!.itemID, item.userID, item.name, item.price)
        }
        itemDetailViewModel.requestBuyResult.observe(this, Observer {
            it?.let {
                if (it.resourceState == ResourceState.LOADING) {
                    loadingLayout.visibility = View.VISIBLE
                } else {
                    loadingLayout.visibility = View.GONE
                    if (it.resourceState == ResourceState.SUCCESS) {
                        Toast.makeText(applicationContext,R.string.request_transaction_completed,Toast.LENGTH_LONG).show()
                    }else if(it.resourceState  == ResourceState.ERROR){
                        Toast.makeText(applicationContext,R.string.request_transaction_err,Toast.LENGTH_LONG).show()

                    }
                }

            }
        })
    }

    private fun getDataFromIntent() {
        action = intent.action
        if (action == ACTION_NORMAL) {
            if (intent.hasExtra("itemBundle")) {
                item = intent.getBundleExtra("itemBundle").getParcelable("item")
                showItem()
            }
        } else if (action == ACTION_SHOW_FROM_ID || action == Constant.ACTION_REQUEST_TRANSACTION) {
            val itemID = intent.getStringExtra("itemID")
            itemDetailViewModel.getItemDetail(itemID)
        }
    }

    private fun showItem() {
        item?.apply {
            if (userID == sharePreferencHelper.getString(Constant.USER_ID, null)) {
                btnActionContact.setBackgroundResource(R.drawable.bg_btn_follow)
                btnActionContact.text = getString(R.string.action_edit)
                checkboxMark.visibility = View.GONE
                btnRequestConfirm.visibility = View.GONE
            } else {
                btnActionContact.setOnClickListener {
                    itemDetailViewModel.getPhone(userID)
                }
                btnRequestConfirm.setOnClickListener {

                }
            }
            viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
            images?.forEach { element ->
                viewPagerAdapter.addFragment(FragmentImage.newInstance(element), "")
            }
            if (images == null || images.isEmpty()) {
                imageNotFound.visibility = View.VISIBLE
            }
            viewPager.adapter = viewPagerAdapter
            viewBinding = DataBindingUtil.bind(coordinatorLayout)
            viewBinding?.item = item
            viewBinding?.executePendingBindings()
            viewBinding!!.time.text = Util.convertTime(item.updatedAt, applicationContext)
            txtPrice.text = Util.convertPriceToText(item.price, applicationContext)

        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            action = it.action

            if (action != null && action == ACTION_SHOW_FROM_ID) {
                val itemID = intent!!.getStringExtra("itemID")
                itemDetailViewModel.getItemDetail(itemID)
            }
        }
    }
}