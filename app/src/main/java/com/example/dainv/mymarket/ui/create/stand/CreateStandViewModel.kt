package com.example.dainv.mymarket.ui.create.stand

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.dainv.mymarket.repository.StandRepository
import com.example.dainv.mymarket.util.ImageHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class CreateStandViewModel @Inject constructor(
        private val standRepository: StandRepository,
        private val context: Context
) : ViewModel() {
    private val createTrigger = MutableLiveData<MultipartBody>()
    val createResult = Transformations.switchMap(createTrigger) {
        return@switchMap standRepository.createStand(it)
    }

    fun createStand(name: String, desciption: String, imagePath: String?) {
        val multiPartBuilder = MultipartBody.Builder()
        imagePath?.let {
            var file = File(imagePath)
            if (file.exists()) {
                file = ImageHelper.reduceImageSize(file, 1080)
                var mimeType: String? = null
                val uri = Uri.fromFile(file)
                mimeType = if (Uri.fromFile(file).scheme == ContentResolver.SCHEME_CONTENT) {
                    val cr = context.contentResolver
                    cr.getType(uri)
                } else {
                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                            .toString())
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                            fileExtension.toLowerCase())
                }
                multiPartBuilder.addFormDataPart("image"
                        , file.name
                        , RequestBody.create(MediaType.parse(mimeType), file))

            }
        }
        multiPartBuilder.addFormDataPart("name",name)
        multiPartBuilder.addFormDataPart("description",desciption)
        multiPartBuilder.setType(MultipartBody.FORM)
        createTrigger.value = multiPartBuilder.build()
    }
}