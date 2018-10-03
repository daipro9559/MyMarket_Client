package com.example.dainv.mymarket.util

import android.content.Context
import android.content.SharedPreferences
import com.example.dainv.mymarket.base.Constant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharePreferencHelper
@Inject constructor(context: Context){
    private  val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constant.APP_NAME,Context.MODE_PRIVATE)
    fun getString(key:String,default:String?):String?{
      return  sharedPreferences.getString(key,default)
    }
    fun putString(key: String,value:String?){
        sharedPreferences.edit().apply{
            this.putString(key,value).commit()
        }
    }


}