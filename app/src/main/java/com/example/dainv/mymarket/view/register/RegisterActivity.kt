package com.example.dainv.mymarket.view.register

import android.graphics.Color
import android.os.Bundle
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.R
import jp.wasabeef.blurry.Blurry
import jp.wasabeef.blurry.internal.Blur
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        Blurry.with(this).radius(10)
                .sampling(8)
                .color(Color.argb(66, 255, 255, 0))
                .async()
                .onto(rootView)
    }
}