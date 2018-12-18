package com.example.dainv.mymarket.util

import android.content.Context
import android.content.SharedPreferences
import com.example.dainv.mymarket.base.Constant
import javax.inject.Inject
import javax.inject.Singleton

class SharePreferencHelper
@Inject constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE)
    fun getString(key: String, default: String?): String? {
        return sharedPreferences.getString(key, default)
    }

    fun putString(key: String, value: String?) {
        sharedPreferences.edit().apply {
            this.putString(key, value).commit()
        }
    }

    fun getInt(key: String) = sharedPreferences.getInt(key, -1)
    fun putInt(key: String, value: Int) = sharedPreferences.edit().let {
        it.putInt(key, value).commit()
    }

    fun putBoolean(key: String, value: Boolean) = sharedPreferences.edit().let {
        it.putBoolean(key, value).commit()
    }

    fun getBoolean(key:String,valueDefault: Boolean) = sharedPreferences.getBoolean(key,valueDefault)

    fun getBoolean(key:String) = sharedPreferences.getBoolean(key,false)
    // clear all after logout
    fun clearAll(){
        sharedPreferences.edit().clear().commit()
    }
}