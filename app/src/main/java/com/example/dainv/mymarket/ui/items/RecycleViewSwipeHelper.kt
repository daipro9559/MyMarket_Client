package com.example.dainv.mymarket.ui.items

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.example.dainv.mymarket.R
import io.reactivex.subjects.PublishSubject

class RecycleViewSwipeHelper : ItemTouchHelper.SimpleCallback {
    private val context: Context
    private val background: ColorDrawable
    private val icon: Drawable
    // publish position of item_view_pager is swiped
    val onSwipedCompleted = PublishSubject.create<Int>()

    constructor(context: Context) : super(0, ItemTouchHelper.LEFT ) {
        this.context = context
        background = ColorDrawable(Color.parseColor("#d32f2f"))
        icon = ContextCompat.getDrawable(context, R.drawable.ic_garbage)!!

    }

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        onSwipedCompleted.onNext(p0.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView.findViewById<View>(R.id.rootView)
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView

        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
            val iconRight = itemView.left + iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(itemView.left, itemView.top,
                    (itemView.left + (dX) + backgroundCornerOffset).toInt(), itemView.bottom)
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(itemView.right + (dX).toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom)
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
    }
}