package com.example.dainv.mymarket.ui.stand.detail

import android.os.Bundle
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseFragment

class StandInformationFragment :BaseFragment(){
    companion object {
        fun newInstance(): StandInformationFragment {
            val standInformationFragment = StandInformationFragment()
            return standInformationFragment
        }
    }
    override fun getLayoutID() = R.layout.fragment_stand_infor

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    private fun initView(){

    }

}
