package com.example.dainv.mymarket.ui.stand.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.view.Menu
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.glide.GlideApp
import com.example.dainv.mymarket.model.Stand
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import com.example.dainv.mymarket.ui.items.ItemAdapter
import kotlinx.android.synthetic.main.activity_stand.*
import javax.inject.Inject

class StandDetailActivity : BaseActivity() {
    companion object {
        val IS_MY_STAND = "is_my_stand"
        val STAND_KEY = "stand key"
    }

    private var stand: Stand? = null
    private var isMystand: Boolean = false
    lateinit var standDetailViewModel: StandDetailViewModel
    @Inject
    lateinit var itemAdapter: ItemAdapter
    private var currentPage = 0
    private var isLoadMore = false
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stand)
        setSupportActionBar(toolBar)
        standDetailViewModel = ViewModelProviders.of(this, viewModelFactory)[StandDetailViewModel::class.java]
        enableHomeBack()
        // get stand
        getDataFromItem()
        initView()
    }

    private fun initView() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(StandDetailFragment.newInstance(),getString(R.string.item))
        viewPagerAdapter.addFragment(StandInformationFragment.newInstance(),getString(R.string.information))
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2
        tabBar.setupWithViewPager(viewPager)
       title = stand?.name
        GlideApp.with(applicationContext)
                .load(Constant.BASE_URL + stand?.image)
                .into(imageCollapse)
        if (!isMystand){
            floatAdd.hide()
        }
        floatAdd.setOnClickListener {
            val intent =  Intent(this,AddItemActivity::class.java)
            intent.putExtra(AddItemActivity.ADDRESS_ID,stand!!.addressID)
            intent.putExtra(AddItemActivity.STAND_ID,stand!!.standID)
            startActivity(intent)
        }
    }

    private fun getDataFromItem() {
        stand = intent.getParcelableExtra(STAND_KEY)
        isMystand = intent.getBooleanExtra(IS_MY_STAND, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isMystand){
            menuInflater.inflate(R.menu.menu_delete,menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }
}