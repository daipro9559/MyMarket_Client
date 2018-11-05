package com.example.dainv.mymarket.ui.items

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MyBottomSheetBehavior<V : View> : BottomSheetBehavior<V>  {
    constructor() : super()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    companion object {
        fun <V : View> from(view: V): MyBottomSheetBehavior<V> {
            val params = view.layoutParams
            if (params !is android.support.design.widget.CoordinatorLayout.LayoutParams) {
                throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            } else {
                val behavior = params.behavior
                return behavior as? MyBottomSheetBehavior<V>
                        ?: throw IllegalArgumentException("The view is not associated with BottomSheetBehavior")
            }
        }
    }

}