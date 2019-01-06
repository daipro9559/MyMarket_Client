package com.example.dainv.mymarket.ui.map

import android.content.Context
import android.content.ContextWrapper
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dainv.mymarket.R


class MenuAdapter(context: Context, resource: Int) : ArrayAdapter<String>(context, resource) {
    // get position from map fragment
    private var positionChecked: Int = 0

    init {
        positionChecked = 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewholder: Viewholder
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_layout, parent, false)
            viewholder = Viewholder()
            viewholder.imgIcon = convertView!!.findViewById(R.id.img_icon)
            viewholder.textView = convertView.findViewById(R.id.text_menu)
            viewholder.rootView = convertView.findViewById(R.id.root_view_menu)
            convertView.tag = viewholder
        } else {
            viewholder = convertView.tag as Viewholder
        }
        if (position == positionChecked) {
            viewholder.rootView!!.background = ContextCompat.getDrawable(parent.context,R.drawable.bg_menu_selected)
        } else {
            viewholder.rootView!!.background = ContextCompat.getDrawable(parent.context,R.drawable.bg_menu_unselected)

        }
        viewholder.imgIcon!!.setImageDrawable(parent.context.getDrawable(iconId[position]))
        viewholder.textView!!.text = parent.context.getString(title[position])

        return convertView
    }


    internal class Viewholder {
        var imgIcon: ImageView? = null
        var textView: TextView? = null

        var rootView: LinearLayout? = null
    }

    fun changePositionSelect(position: Int) {
        positionChecked = position
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return iconId.size
    }

    companion object {
        private val iconId = intArrayOf(R.drawable.ic_map_normal, R.drawable.ic_map_satellite, R.drawable.ic_map_terrain)
        private val title = intArrayOf(R.string.map_type_normal, R.string.map_type_satellite, R.string.map_type_terrain)
    }
}
