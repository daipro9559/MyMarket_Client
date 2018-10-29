package com.example.dainv.mymarket.ui.additem

import android.os.Bundle
import com.example.dainv.mymarket.base.BaseDialog
import com.example.dainv.mymarket.base.BaseDialogSelect
import com.example.dainv.mymarket.model.Province
import dagger.Lazy
import kotlinx.android.synthetic.main.dialog_select.*
import javax.inject.Inject

class DialogSelectProvince :BaseDialogSelect<Province>() {
    companion object {
        val LIST_PROVINCE = "key_province_list"
        fun newInstance(provinces:ArrayList<Province>) :DialogSelectProvince{
            val bundle = Bundle()
            bundle.putParcelableArrayList(LIST_PROVINCE,provinces)
            val selectProvince = DialogSelectProvince()
            selectProvince.arguments = bundle
            return selectProvince
        }
    }
    @Inject lateinit var adapterSelectProvince: Lazy<AdapterSelectProvince>
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun initView() {
        super.initView()
        adapterSelectProvince.get().swapItems(arguments!!.getParcelableArrayList(LIST_PROVINCE))
        recycleView.adapter = adapterSelectProvince.get()
    }

    fun getItemClick() =  adapterSelectProvince.get().itemCLickObserve()
}