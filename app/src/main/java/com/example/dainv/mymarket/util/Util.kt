package com.example.dainv.mymarket.util

import android.content.Context
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
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
}