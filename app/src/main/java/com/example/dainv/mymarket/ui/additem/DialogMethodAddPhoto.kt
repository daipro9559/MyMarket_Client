package com.example.dainv.mymarket.ui.additem

import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.util.ClickCallback
import kotlinx.android.synthetic.main.dialog_bottom_select_contact.*

class DialogMethodAddPhoto :BottomSheetDialogFragment(){

    companion object {
        const val TITLE_KEY="title key"
        val TAG = "DialogMethodAddPhoto"
        fun newsInstance(title:String): DialogMethodAddPhoto {
            val bundle = Bundle()
            bundle.putString(TITLE_KEY,title)
            val dialogMethodAddPhoto = DialogMethodAddPhoto()
            dialogMethodAddPhoto.arguments  = bundle
            return dialogMethodAddPhoto
        }
    }
    lateinit var clickCallback :ClickCallback<Int>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.dialog_bottom_select_contact,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rootView.setBackgroundColor(context!!.resources.getColor(R.color.colorWhite,activity!!.theme))
        }else{
            rootView.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorWhite))
        }
        title.text = arguments!!.getString(TITLE_KEY,"")
        icOne.setBackgroundResource(R.drawable.ic_take_photo)
        titleOne.setText(R.string.take_new_photo)
        actionOne.setOnClickListener {
            clickCallback.invoke(actionOne.id)
        }
        icTwo.setBackgroundResource(R.drawable.ic_gallery)
        titleTwo.setText(R.string.choose_image_from_gallery)
        actionTwo.setOnClickListener {
            clickCallback.invoke(actionTwo.id)
        }
    }
}