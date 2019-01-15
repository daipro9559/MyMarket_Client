package com.example.dainv.mymarket.ui.map

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.constant.BindViewStatic
import com.makeramen.roundedimageview.RoundedImageView
import io.reactivex.subjects.PublishSubject

class VerticalAdapter(private val images : List<String>,private val context: Context) : PagerAdapter() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    val imageClickObserve = PublishSubject.create<String>()
    override fun getCount(): Int {
        return images?.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mLayoutInflater.inflate(R.layout.item_view_pager, container, false)
        view.setOnClickListener {
            imageClickObserve.onNext(images[position])
        }
        val imageRound = view.findViewById<RoundedImageView>(R.id.image)
        BindViewStatic.showImage(imageRound,images[position])
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
