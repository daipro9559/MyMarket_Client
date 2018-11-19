package com.example.dainv.mymarket.util

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.District
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object Util {
    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // Create an image file provinceName
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val fileDes = File(storageDir.absolutePath + "/Camera/MyMarket")
        if (!fileDes.exists()){
            fileDes.mkdirs()
        }
        return File.createTempFile(
                "JPEG_$timeStamp",  // prefix
                ".jpg",         // suffix
                fileDes    // directory
        )
    }

    fun convertPriceToFormat(price: Long): String {
        return "${NumberFormat.getNumberInstance(Locale.GERMAN).format(price)} đ"
//        return "${android.icu.text.NumberFormat.getInstance(android.icu.text.NumberFormat.ACCOUNTINGCURRENCYSTYLE) .format(price)}"
    }

    fun categoryAll(context: Context) = Category(0,context.getString(R.string.all_category),"")
    fun districtAll(context: Context) = District(0,context.getString(R.string.all),0)

     fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        return try {
            val columnsQuery = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, columnsQuery, null, null, null)
            val pathIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            cursor!!.getString(pathIndex)
        } catch (e: Exception) {
            ""
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
    }
//    2018-11-15T00:20:50.000Z
    fun convertTime(time:String,context: Context):String{
        val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // need get date to compare
        val currenTimeFormat = timeFormat.format(Date())
        if (time.substring(0,10) == currenTimeFormat.substring(0,10)){
            return context.getString(R.string.today,time.substring(11,16))
        }else if(time.substring(0,4) == currenTimeFormat.substring(0,4)){
            return time.substring(5,10) + ", "+time.substring(11,16)
        }else{
            return time.substring(0,10) +", "+time.substring(11,16)
        }
    }
}