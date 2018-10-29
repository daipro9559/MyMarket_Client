package com.example.dainv.mymarket.base

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.dainv.mymarket.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_select.*

abstract class BaseDialogSelect<I> : BaseDialog() {
    override fun getLayoutId() = R.layout.dialog_select
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }
    open fun initView(){
        recycleView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

    }

}