package com.example.dainv.mymarket.ui.stand.detail

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.glide.GlideApp
import com.example.dainv.mymarket.model.Stand
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import com.example.dainv.mymarket.ui.common.ViewPagerAdapter
import com.example.dainv.mymarket.ui.marked.ItemAdapter
import com.example.dainv.mymarket.ui.transaction.TransactionActivity
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
        viewPagerAdapter.addFragment(StandDetailFragment.newInstance(), getString(R.string.item))
        viewPagerAdapter.addFragment(StandInformationFragment.newInstance(), getString(R.string.information))
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2
        tabBar.setupWithViewPager(viewPager)
        title = stand?.name
        stand?.let {
            standDetailViewModel.standID = it.standID
        }
        GlideApp.with(applicationContext)
                .load(Constant.BASE_URL + stand!!.image[0])
                .into(imageCollapse)

    }

    private fun getDataFromItem() {
        stand = intent.getParcelableExtra(STAND_KEY)
        isMystand = intent.getBooleanExtra(IS_MY_STAND, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isMystand) {
            menuInflater.inflate(R.menu.menu_delete, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_delete_stand -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == StandDetailFragment.REQUEST_ADD_ITEM_TO_STAND){
//            if (resultCode == Activity.RESULT_OK){
//                (viewPagerAdapter.getItem(0) as StandDetailFragment).fetchItems()
//            }
//        }
//    }
}