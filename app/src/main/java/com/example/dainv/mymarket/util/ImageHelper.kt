package com.example.dainv.mymarket.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object  ImageHelper{
    fun reduceImageSize(file: File,sizeMax :Int):File{
        val optionScale = BitmapFactory.Options()
        optionScale.inJustDecodeBounds = true
        optionScale.inSampleSize = 1

        var inputStream  = FileInputStream(file)
        BitmapFactory.decodeStream(inputStream,null,optionScale)
        var scale = 1
        while (optionScale.outWidth /scale >= sizeMax && optionScale.outHeight >= sizeMax ){
            scale *=2
        }
        val optionWrite = BitmapFactory.Options()
        optionWrite.inSampleSize = scale
        inputStream = FileInputStream(file)
        val bitmapSelect = BitmapFactory.decodeStream(inputStream,null,optionWrite)
        inputStream.close()
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        bitmapSelect.compress(Bitmap.CompressFormat.JPEG,100,outputStream)
        outputStream.close()
        return file
    }
}