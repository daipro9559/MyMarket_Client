package com.example.dainv.mymarket.ui.itemdetail

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.util.ClickCallback
import kotlinx.android.synthetic.main.dialog_select_contact.*

class BottomDialogSelectContact : BottomSheetDialogFragment(){
    companion object {
        fun newsInstance(): BottomDialogSelectContact{
            val bundle = Bundle()
            val bottomSheetDialogFragment = BottomDialogSelectContact()
            bottomSheetDialogFragment.arguments  = bundle
            return bottomSheetDialogFragment
        }
    }
    lateinit var clickCallback :ClickCallback<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.dialog_select_contact,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionCall.setOnClickListener {
            clickCallback.invoke(it.id)
        }
        actionSend.setOnClickListener {
            clickCallback.invoke(it.id)
        }
    }
}