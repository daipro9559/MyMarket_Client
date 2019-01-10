package com.example.dainv.mymarket.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.constant.Constant
import com.example.dainv.mymarket.entity.ErrorResponse
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.login.LoginActivity
import com.example.dainv.mymarket.util.MyViewModelFactory
import com.example.dainv.mymarket.util.SharePreferencHelper
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment : Fragment(){
    @Inject
    protected lateinit var viewModelFactory: MyViewModelFactory
    @Inject lateinit var sharePreferencHelper: SharePreferencHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(getLayoutID(),container,false)
    }
    protected abstract fun getLayoutID():Int

    protected fun logout(){
        sharePreferencHelper.putString(Constant.TOKEN,null)
        val intent = Intent(activity,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity!!.finish()
    }

    fun startActivityWithAnimation(intent :Intent){
        startActivity(intent)
        activity?.apply{
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
    protected fun viewLoading(resourceState: ResourceState, view: View){
        if (resourceState == ResourceState.LOADING){
            view.visibility = View.VISIBLE
        }else{
            view.visibility = View.GONE
        }
    }
    protected fun handleErrorApiResponse(errorResponse: ErrorResponse){
        if( activity!! is BaseActivity){
            (activity as BaseActivity).handelErrResponseApi(errorResponse)
        }
    }
}