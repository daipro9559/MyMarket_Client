package com.example.dainv.mymarket.ui.addAddress

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.District
import com.example.dainv.mymarket.model.Province
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.additem.AddItemViewModel
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_add_address.*

class AddAddressActivity : BaseActivity() {

    private var provinceSelect: Province? = null
    private var districtSelect: District? =null
    private lateinit var addAdressViewModel: AddAdressViewModel
    private lateinit var addItemViewModel: AddItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        addAdressViewModel = ViewModelProviders.of(this, viewModelFactory)[AddAdressViewModel::class.java]
        addItemViewModel = ViewModelProviders.of(this, viewModelFactory)[AddItemViewModel::class.java]
        initView()

        addItemViewModel.districtLiveData.observe(this, Observer {
            if (it!!.resourceState == ResourceState.SUCCESS) {
                val dialogSelectDistrict = DialogSelectDistrict.newInstance(it.r!!)
                dialogSelectDistrict.callback = {
                    districtSelect = it
                    txtDistrict.text = it.districtName
                }
                dialogSelectDistrict.show(supportFragmentManager, DialogSelectDistrict.TAG)
            }
        })
        addAdressViewModel.addAddressResult.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING){
                letsGo.visibility = View.GONE
                loading.visibility =View.VISIBLE
            }else{
                letsGo.visibility = View.VISIBLE
                loading.visibility =View.GONE
                if(it.resourceState == ResourceState.SUCCESS){
                    startActivityWithAnimation(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(applicationContext,R.string.save_err,Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun initView() {
        cardProvince.setOnClickListener {
            addItemViewModel.getAllProvince().observe(this, Observer {
                it!!.r?.let {
                    val dialogSelectProvince = DialogSelectProvince.newInstance(it)
                    dialogSelectProvince.callback = {
                        provinceSelect = it
                        txtProvince.text = it.provinceName
                        cardDistrict.isEnabled = true
                    }
                    dialogSelectProvince.show(supportFragmentManager, DialogSelectProvince.TAG)
                }
            })

        }
        cardDistrict.setOnClickListener {
            if (provinceSelect == null){
                Toast.makeText(applicationContext,R.string.need_select_province_before,Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            addItemViewModel.getDistricts(provinceSelect!!.provinceID)
        }
        letsGo.setOnClickListener {
            if (districtSelect == null
                    || provinceSelect == null
                    || edtAddress.text.isNullOrEmpty()) {
                Toast.makeText(applicationContext,R.string.please_input_full_information,Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            addAdressViewModel.confirmAddress(edtAddress.text.toString(),provinceSelect!!.provinceID,districtSelect!!.districtID)
        }
    }
}