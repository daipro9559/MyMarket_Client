package com.example.dainv.mymarket.ui.dialog

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseDialogSelect
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.Province
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.ui.additem.AddItemViewModel
import dagger.Lazy
import kotlinx.android.synthetic.main.dialog_select.*
import javax.inject.Inject

class DialogSelectProvince : BaseDialogSelect<Province>(){
    companion object {
        val TAG = "dialog select province"
        fun newInstance() :DialogSelectProvince{
            val dialogSelectProvince = DialogSelectProvince()
            return dialogSelectProvince
        }
    }
    lateinit var addItemViewModel: AddItemViewModel
    @Inject lateinit var adapterSelectProvince: Lazy<AdapterSelectProvince>
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        titleDialog.text = getString(R.string.select_province)
        addItemViewModel = ViewModelProviders.of(activity!!,viewModelFactory)[AddItemViewModel::class.java]
        addItemViewModel.getAllProvince().observe(this, Observer {
            if (it!!.resourceState == ResourceState.SUCCESS){
                adapterSelectProvince.get().swapItems(it.r!!)
            }
        })
    }

    override fun initView() {
        super.initView()
        recycleView.adapter = adapterSelectProvince.get()
        adapterSelectProvince.get().itemCLickObserve().subscribe{
            callback.invoke(it)
            dismiss()
        }
    }
}