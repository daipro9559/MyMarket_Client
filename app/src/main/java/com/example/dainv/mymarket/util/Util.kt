package com.example.dainv.mymarket.util

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.model.Category
import com.example.dainv.mymarket.model.District
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import android.net.ConnectivityManager
import android.util.TypedValue
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import org.json.JSONObject


object Util {
    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // Create an image file provinceName
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val fileDes = File(storageDir.absolutePath + "/Camera/MyMarket")
        if (!fileDes.exists()) {
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

    fun convertPriceToText(price: Long, context: Context): String {
        var priceFloat = price.toFloat()
        if (price in 1001..999999) {
            priceFloat /= 1000
            return context.getString(R.string.thosand, priceFloat.toString())
        }
        if (price >= 1000000) {
            priceFloat /= 1000000
            return context.getString(R.string.bilion, priceFloat.toString())
        }
        return convertPriceToFormat(price)
    }

    fun categoryAll(context: Context) = Category(0, context.getString(R.string.all_category), "")
    fun districtAll(context: Context) = District(0, context.getString(R.string.all), 0)

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
    fun convertTime(time: String, context: Context): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val timeInputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        timeInputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = timeInputFormat.parse(time)
        val millis = date.time
        calendar.timeInMillis = millis
//        val month = if (calendar.get(Calendar.MONTH)<9)  "0"+(calendar.get(Calendar.MONTH)+1).toString() else (calendar.get(Calendar.MONTH)+1).toString()
//        val timeConverted = "${calendar.get(Calendar.YEAR)}-$month-${calendar.get(Calendar.DATE)} " +
//                "${calendar[Calendar.HOUR]}:${calendar.get(Calendar.MINUTE)}:${calendar.get(Calendar.SECOND)}"
        val myTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // need get date to compare
        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        currentCalendar.timeInMillis = Date().time
        val currentTimeFormat = myTimeFormat.format(Date())
        if (calendar[Calendar.YEAR] == currentCalendar[Calendar.YEAR]
                && calendar[Calendar.MONTH] == currentCalendar[Calendar.MONTH]
                && calendar[Calendar.DATE] == currentCalendar[Calendar.DATE]) {
            if (calendar[Calendar.HOUR_OF_DAY] == currentCalendar[Calendar.HOUR_OF_DAY]) {
                return context.getString(R.string.yet)
            } else {
                return context.getString(R.string.today, "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}")
            }
        } else if (calendar[Calendar.YEAR] == currentCalendar[Calendar.YEAR]
                && calendar[Calendar.MONTH] == currentCalendar[Calendar.MONTH]
                && calendar[Calendar.DATE] == currentCalendar[Calendar.DATE] - 1) {
            return context.getString(R.string.yesterday, "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}")
        } else {
            val month = if (calendar.get(Calendar.MONTH) < 9) "0" + (calendar.get(Calendar.MONTH) + 1).toString() else (calendar.get(Calendar.MONTH) + 1).toString()
            val timeShow = "${calendar.get(Calendar.YEAR)}-$month-${calendar.get(Calendar.DATE)} " +
                    "${calendar[Calendar.HOUR_OF_DAY]}:${calendar.get(Calendar.MINUTE)}"
            return timeShow
        }
    }

    fun convertDpToPx(value: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics).toInt()
    }

    fun isOnline(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = manager.activeNetworkInfo
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            // Network is present and connected
            isAvailable = true
        }
        return isAvailable
    }

    fun buildIntentForNotification(jsonObject: JSONObject, context: Context): Intent {
        val intent = Intent(context, ItemDetailActivity::class.java)
        val type = jsonObject.getInt("type")
        intent.putExtra("itemID", jsonObject.getString("itemID"))
        if (type == 1){
            intent.action = ItemDetailActivity.ACTION_SHOW_FROM_ID
        }
        if (type == 2) {
            intent.putExtra("standID", jsonObject.getString("standID"))
            intent.action = ItemDetailActivity.ACTION_SHOW_FROM_ID
        }else if (type == 3){
            intent.putExtra("buyerID",jsonObject.getString("userID"))
            intent.putExtra("price",jsonObject.getString("price"))
            intent.action = Constant.ACTION_REQUEST_TRANSACTION
        }
        return intent
    }
}