package com.example.dainv.mymarket.ui.itemdetail

import android.os.Bundle
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseFragment
import com.example.dainv.mymarket.constant.BindViewStatic
import kotlinx.android.synthetic.main.fragment_image.*

class FragmentImage : BaseFragment(){
    private lateinit var imageUrl: String
    companion object {
        val IMAGE_PATH_KEY = "image path key"
        fun newInstance(image:String):FragmentImage{
            val bundle = Bundle()
            bundle.putString(IMAGE_PATH_KEY,image)
            val fragmentImage = FragmentImage()
            fragmentImage.arguments = bundle
            return fragmentImage
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageUrl = arguments!!.getString(IMAGE_PATH_KEY)
    }
    override fun getLayoutID() = R.layout.fragment_image

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        BindViewStatic.showImage(imageView,imageUrl)
    }
}