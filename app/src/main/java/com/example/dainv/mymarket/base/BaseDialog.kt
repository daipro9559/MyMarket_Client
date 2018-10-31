package com.example.dainv.mymarket.base

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.dainv.mymarket.util.MyViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import android.os.Build
import com.example.dainv.mymarket.R


abstract class BaseDialog : DialogFragment(){
    @Inject protected lateinit var viewModelFactory: MyViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.DialogAppStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(getLayoutId(),container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val title = view!!.findViewById<View>(R.id.title)
            if (title != null) {
                title.visibility = View.GONE
            }
        }
        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
//        dialog.window.setBackgroundDrawableResource(R.color.colorTransparent)
        dialog.setCanceledOnTouchOutside(true)
    }
    protected abstract fun getLayoutId():Int
}