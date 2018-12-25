package com.example.dainv.mymarket.ui.dialog

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Parcelable
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseDialogSelect
import com.example.dainv.mymarket.entity.Province
import dagger.Lazy
import kotlinx.android.synthetic.main.dialog_select.*
import java.util.ArrayList
import javax.inject.Inject

class DialogSelectProvince : BaseDialogSelect<Province>() {
    companion object {
        val TAG = "dialog select province"
        val KEY_LIST_PROVINCE = "key_list_province"
        fun newInstance(): DialogSelectProvince {
            val dialogSelectProvince = DialogSelectProvince()
            return dialogSelectProvince
        }

        fun newInstance(list: List<Province>): DialogSelectProvince {
            val bundle = Bundle()
            val dialogSelectProvince = DialogSelectProvince()
            bundle.putParcelableArrayList(KEY_LIST_PROVINCE, list as ArrayList<out Parcelable>)
            dialogSelectProvince.arguments =bundle
            return dialogSelectProvince
        }
    }

    @Inject
    lateinit var adapterSelectProvince: Lazy<AdapterSelectProvince>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        titleDialog.text = getString(R.string.select_province)
        adapterSelectProvince.get().swapItems(arguments!!.getParcelableArrayList(KEY_LIST_PROVINCE))

    }

    override fun initView() {
        super.initView()
        recycleView.adapter = adapterSelectProvince.get()
        adapterSelectProvince.get().itemClickObserve().observe(this, Observer {
                callback.invoke(it!!)
                dismiss()

        })
    }
}