package com.example.dainv.mymarket.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Util {
    @Throws(IOException::class)
     fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        )
    }
    fun convertPriceToFormat(price:Int) : String{
       return "${NumberFormat.getIntegerInstance(Locale.GERMAN).format( price)} đ"
    }
}