package com.example.dainv.mymarket.ui.main.profile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.dainv.mymarket.repository.UserRepository
import com.example.dainv.mymarket.util.ImageHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileViewModel
    @Inject
    constructor(
            private val userRepository: UserRepository,
            private val context: Context
    )
    :ViewModel(){
    private val logoutTrigger = MutableLiveData<Any>()
    private val profileTrigger = MutableLiveData<Any>()
    private val changePassParam = MutableLiveData<ChangePassParam>()
    private val updateAvatarTrigger = MutableLiveData<String>()
    val profileLiveData  =  Transformations.switchMap(profileTrigger){
        return@switchMap userRepository.getMyProfile()
    }
    val logoutResult = Transformations.switchMap(logoutTrigger){
        return@switchMap userRepository.logout()
    }
    val changePassResultLiveData = Transformations.switchMap(changePassParam){
        userRepository.changePassword(it.oldPass,it.newPass)
    }!!
    val avatarUpdateLiveData = Transformations.switchMap(updateAvatarTrigger){
        userRepository.updateproFile(createMutiplePartFile(it))
    }!!

    fun getProfile(){
        profileTrigger.value = ""
    }

    fun updateAvatar(imagePath:String){
        updateAvatarTrigger.value = imagePath
    }

    fun logout(){
        logoutTrigger.value = Any()
    }
    fun changPass(oldPass:String,newPass:String){
        changePassParam.value = ChangePassParam(oldPass,newPass)
    }

    private fun createMutiplePartFile(urlPath:String): MultipartBody{
        val multiPartBuilder = MultipartBody.Builder()
        var file = File(urlPath)
        if (file.exists()){
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
        multiPartBuilder.setType(MultipartBody.FORM)
        return multiPartBuilder.build()
    }

   data class ChangePassParam(
        val oldPass:String,
        val newPass:String    )
}