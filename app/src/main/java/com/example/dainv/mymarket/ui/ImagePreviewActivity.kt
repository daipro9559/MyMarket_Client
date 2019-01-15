package com.example.dainv.mymarket.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.constant.BindViewStatic
import com.example.dainv.mymarket.glide.GlideApp
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreviewActivity : AppCompatActivity(){
    companion object {
        val IMAGE_URL_KEY="image url key"
    }
    private lateinit var imageUrl :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_image_preview)
        if(intent.hasExtra(IMAGE_URL_KEY)){
            imageUrl = intent.getStringExtra(IMAGE_URL_KEY)
            BindViewStatic.showImage(imageView,imageUrl)
        }
    }
}