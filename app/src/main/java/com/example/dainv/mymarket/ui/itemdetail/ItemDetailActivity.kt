package com.example.dainv.mymarket.ui.itemdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.databinding.ActivityItemDetailBinding
import com.example.dainv.mymarket.model.Item
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.common.ViewPagerAdapter
import com.example.dainv.mymarket.util.Util
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class ItemDetailActivity :BaseActivity() {
    private lateinit var item:Item
    private  var viewBinding: ActivityItemDetailBinding?=null

    private lateinit var itemDetailViewModel: ItemDetailViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        toolBar.setTitle(R.string.detail_item_activity_title)
        setSupportActionBar(toolBar)
        enableHomeBack()
        itemDetailViewModel = ViewModelProviders.of(this,viewModelFactory)[ItemDetailViewModel::class.java]
        val myIntent = intent
        item = myIntent.getBundleExtra("itemBundle").getParcelable("item")
        item?.apply{
            viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
            images.forEach { element ->
                viewPagerAdapter.addFragment(FragmentImage.newInstance(element),"")
            }
            viewPager.adapter = viewPagerAdapter
            viewBinding = DataBindingUtil.bind(coordinatorLayout)
            viewBinding?.item =item
            viewBinding?.executePendingBindings()
            txtPrice.text = Util.convertPriceToFormat(item.price)
            btnActionContact.setOnClickListener {
                itemDetailViewModel.getPhone(userID)
            }
        }
        itemDetailViewModel.phoneDataLiveData.observe(this, Observer {
            if (it!!.resourceState == ResourceState.SUCCESS){
                val phone  = it.r
                val bottomDialogSelectContact = BottomDialogSelectContact.newsInstance()
                bottomDialogSelectContact.clickCallback ={
                    val intent = Intent()
                    if (it == R.id.actionOne){
                        intent.action = Intent.ACTION_DIAL
                        val data = "tel:$phone"
                        intent.data = Uri.parse(data)

                    }else{//action message
                        intent.action = Intent.ACTION_SENDTO
                        val data = "smsto:$phone"
                        intent.data  = Uri.parse(data)
                    }
                    startActivity(intent)
                    bottomDialogSelectContact.dismiss()
                }
                supportFragmentManager.beginTransaction().add(bottomDialogSelectContact,"").disallowAddToBackStack().commit()
            }
        })
    }
}