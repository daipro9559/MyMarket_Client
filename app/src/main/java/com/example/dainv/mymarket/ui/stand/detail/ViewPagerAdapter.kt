package com.example.dainv.mymarket.ui.stand.detail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) :FragmentPagerAdapter(fragmentManager) {

    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()
    override fun getItem(p0: Int): Fragment {
       return  fragments[p0]
    }

    override fun getCount(): Int {
        return fragments.size
    }
    fun addFragment(fragment: Fragment,title:String){
        if (fragment == null){
            return
        }
        fragments.add(fragment)
        titles.add(title)

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}