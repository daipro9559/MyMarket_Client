package com.example.dainv.mymarket.ui.stand.detail

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.glide.GlideApp
import com.example.dainv.mymarket.entity.Stand
import com.example.dainv.mymarket.ui.common.ViewPagerAdapter
import com.example.dainv.mymarket.ui.create.stand.CreateStandActivity
import kotlinx.android.synthetic.main.activity_stand.*

class StandDetailActivity : BaseActivity() {
    companion object {
        val IS_MY_STAND = "is_my_stand"
        val STAND_KEY = "stand key"
        const val DELETE_STAND_ACTION = "delete stand action"
        const val REQUEST_CODE_EDIT_STAND = 133
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
        standDetailViewModel.deleteResult.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
                if (it.resourceState == ResourceState.SUCCESS) {
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(DELETE_STAND_ACTION))
                    Toast.makeText(applicationContext, R.string.delete_completed, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })
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
        if (stand!!.image.isNotEmpty()) {
            GlideApp.with(applicationContext)
                    .load(Constant.BASE_URL + stand!!.image[0])
                    .into(imageCollapse)
        }

    }
    private fun bindStandDetail(stand: Stand?){

    }

    private fun getDataFromItem() {
        stand = intent.getParcelableExtra(STAND_KEY)
        isMystand = intent.getBooleanExtra(IS_MY_STAND, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isMystand) {
            menuInflater.inflate(R.menu.menu_stand, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_delete_stand -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.notification)
                        .setMessage(R.string.delete_stand_warning)
                        .setPositiveButton(R.string.confirm) { dialog, which ->
                            standDetailViewModel.deleteStand(stand!!.standID)
                            dialog.cancel()
                        }
                        .setNegativeButton(R.string.cancel) { dialog, which ->
                            dialog.cancel()
                        }
                        .create()
                builder.show()
            }
            R.id.menu_edit_stand -> {
                val intent = Intent(this, CreateStandActivity::class.java)
                intent.action = CreateStandActivity.ACTION_EDIT_STAND
                intent.putExtra(CreateStandActivity.STAND_KEY,stand)
                startActivityForResult(intent, REQUEST_CODE_EDIT_STAND)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_EDIT_STAND) {

            }
        }
    }
}