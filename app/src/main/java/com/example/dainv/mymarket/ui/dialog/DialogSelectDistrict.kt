package com.example.dainv.mymarket.ui.dialog

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Parcelable
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseDialogSelect
import com.example.dainv.mymarket.model.District
import dagger.Lazy
import kotlinx.android.synthetic.main.dialog_select.*
import java.util.ArrayList
import javax.inject.Inject


class DialogSelectDistrict : BaseDialogSelect<District>(){
    companion object {
        val TAG = "dialog select district"
        val LIST_DISTRICT_KEY = "ket_list_district"
        fun newInstance() :DialogSelectDistrict{
            val dialogSelectDistrict = DialogSelectDistrict()
            return dialogSelectDistrict
        }
        fun newInstance(list:List<District>) : DialogSelectDistrict{
            val bundle = Bundle()
            bundle.putParcelableArrayList(LIST_DISTRICT_KEY,list as ArrayList<out Parcelable>)
            val dialogSelectDistrict = DialogSelectDistrict()
            dialogSelectDistrict.arguments = bundle
            return dialogSelectDistrict
        }
    }
//    lateinit var addItemViewModel: AddItemViewModel
    @Inject lateinit var adapterSelectDistrict: Lazy<AdapterSelectDistrict>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        titleDialog.text = getString(R.string.select_district)
//        addItemViewModel = ViewModelProviders.of(activity!!,viewModelFactory)[AddItemViewModel::class.java]
//        addItemViewModel.districtLiveData.observe(this, Observer {
//            if (it!!.resourceState == ResourceState.SUCCESS){
                adapterSelectDistrict.get().swapItems(arguments!!.getParcelableArrayList(LIST_DISTRICT_KEY))
//            }
//        })
    }

    override fun initView() {
        super.initView()
        recycleView.adapter = adapterSelectDistrict.get()
        adapterSelectDistrict.get().itemClickObserve().observe(this, Observer {
            callback.invoke(it!!)
            dismiss()
        })
    }
}