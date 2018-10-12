package com.example.dainv.mymarket.viewcustom

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

class ImageCategory : AppCompatImageView{
    constructor(ctx: Context) : super(ctx)
    constructor(context:Context, attrs: AttributeSet) : super(context,attrs)
    constructor(context:Context,attrs: AttributeSet ,defStyleAttr:Int) : super(context,attrs,defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = widthMeasureSpec
        val desireHeight = desiredWidth/2
        setMeasuredDimension(desiredWidth,desireHeight)
    }
}