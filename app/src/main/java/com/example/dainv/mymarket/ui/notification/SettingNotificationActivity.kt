package com.example.dainv.mymarket.ui.notification

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.entity.*
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import kotlinx.android.synthetic.main.activity_setup_notification.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class SettingNotificationActivity : BaseActivity() {
    private lateinit var notificationSetting: NotificationSetting
    private lateinit var settingViewModel: SettingNotificationViewModel
    private var districtSelected: District? = null
    private var provinceSelected: Province? = null
    private var categorySelected: Category? = null
    private val listDistrict = ArrayList<District>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_notification)
        setSupportActionBar(toolBar)
        enableHomeBack()
        title = getString(R.string.notification_setup)
        settingViewModel = ViewModelProviders.of(this, viewModelFactory)[SettingNotificationViewModel::class.java]
        initView()
        settingViewModel.listDistrictLiveData.observe(this, Observer { resourceWrapper ->
            if (resourceWrapper!!.resourceState == ResourceState.SUCCESS) {
                val arrayList = ArrayList<District>(resourceWrapper.r!!)
                listDistrict.clear()
                listDistrict.addAll(arrayList)
                districtSelected = listDistrict[0]
                txtDistrict.text = districtSelected!!.districtName
            }
        })
        settingViewModel.saveSettingResult.observe(this, Observer {resource->
            if (resource!!.resourceState == ResourceState.LOADING){
                loadingLayout.visibility = View.VISIBLE
            }else{
                loadingLayout.visibility = View.GONE
            }
            resource.r?.success?.let {
                if (it){
                    Toast.makeText(applicationContext,R.string.save_completed,Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(applicationContext,R.string.save_err,Toast.LENGTH_LONG).show()
                }
            }
        })

        settingViewModel.getSettingResult.observe(this, Observer { resource ->
            if (resource!!.resourceState == ResourceState.LOADING){
                loadingLayout.visibility = View.VISIBLE
            }else{
                loadingLayout.visibility = View.GONE

            }
            resource?.r?.data?.let {
                notificationSetting = it
                switchNotification.isChecked = notificationSetting.isEnable
                //get infor setting
                notificationSetting.Category?.let {
                    txtCategory.text = it.categoryName
                    categorySelected = it
                }
                notificationSetting.District?.let {
                    txtDistrict.text = it.districtName
                    districtSelected = it
                }
                notificationSetting.Province?.let {
                    txtProvince.text = it.provinceName
                    provinceSelected = it
                }
            }
        })
    }

    /**
     * TODO : need get default district of province after click province
     */
    private fun initView() {
        cardCategory.setOnClickListener { view ->
            settingViewModel.getAllCategory().observe(this, Observer { resourceWrapper ->
                if (resourceWrapper!!.resourceState == ResourceState.SUCCESS) {
                    val dialogSelectCategory = DialogSelectCategory.newInstance(resourceWrapper.r as ArrayList<Category>)
                    dialogSelectCategory.callback = {
                       categorySelected = it
                        txtCategory.text = it.categoryName
                    }
                    dialogSelectCategory.show(supportFragmentManager, DialogSelectCategory.TAG)
                }
            })

        }
        cardProvince.setOnClickListener { v ->
            settingViewModel.getAllProvince().observe(this, Observer { resourceWrapper ->
                resourceWrapper!!.r?.let { it ->
                    val dialogSelectProvince = DialogSelectProvince.newInstance(it)
                    dialogSelectProvince.callback = {
                        provinceSelected = it
                        txtProvince.text = it.provinceName
                        //reset district after select province
                        settingViewModel.getDistricts(provinceSelected!!.provinceID)

                    }
                    dialogSelectProvince.show(supportFragmentManager, DialogSelectProvince.TAG)
                }
            })

        }
        cardDistrict.setOnClickListener {
            val dialogSelectDistrict = DialogSelectDistrict.newInstance(listDistrict)
            dialogSelectDistrict.callback = {
                districtSelected = it
                txtDistrict.text = it.districtName
            }
            dialogSelectDistrict.show(supportFragmentManager, DialogSelectDistrict.TAG)
        }
        btnSave.setOnClickListener {
            notificationSetting.isEnable = switchNotification.isChecked
            notificationSetting.Category = categorySelected
            notificationSetting.District = districtSelected
            notificationSetting.Province = provinceSelected
            settingViewModel.saveSetting(notificationSetting)
        }
    }
}