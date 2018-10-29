package com.example.dainv.mymarket.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.NetworkRequest
import android.net.Uri
import android.os.PersistableBundle
import android.support.v4.app.JobIntentService
import com.example.dainv.mymarket.model.AddItemBody
import com.example.dainv.mymarket.repository.ItemRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.android.AndroidInjection
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import android.webkit.MimeTypeMap
import android.content.ContentResolver




class UploadService : JobService() {
    val NOTIFY_UPLOAD_CODE = 101

    @Inject
    lateinit var itemRepository: ItemRepository

    @Inject
    lateinit var gson: Gson

    companion object {
        const val ACTION_UPLOAD_ITEM = "action.upload.item"
        const val PERISTABLE_BUNDLE_KEY = "upload.bundle.key"
        const val LIST_IMAGE_PATH = "upload.list.image.path"
        const val ADD_ITEM_BODY_JSON = "upload.add.item.body.json"
        const val JOB_UPLOAD_ID = 150
        fun startService(context: Context, bundle: PersistableBundle) {
            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo: JobInfo
            jobInfo = JobInfo.Builder(JOB_UPLOAD_ID, ComponentName(context, UploadService::class.java))
                    .setExtras(bundle)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                    .setRequiredNetwork(NetworkRequest.Builder()
//                            .build())
                    .build()
            jobScheduler.schedule(jobInfo)
        }
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.e("start job service")
        val listImagePathJson = params!!.extras.getString(LIST_IMAGE_PATH)
        val addItemBodyJson = params!!.extras.getString(ADD_ITEM_BODY_JSON)
        val addItemObject = gson.fromJson<AddItemBody>(addItemBodyJson, AddItemBody::class.java)
        listImagePathJson?.let {
            val listImagePath = gson.fromJson<ArrayList<String>>(listImagePathJson, object : TypeToken<ArrayList<String>>() {}.type)
            uploadItem(addItemObject, listImagePath)
            return@onStartJob true
        }
        uploadItem(addItemObject)
        return true
    }

    private fun uploadItem(addItemBody: AddItemBody, imagesPath: List<String>? = null) {
        val multipartBody = createMultipleImagePart(imagesPath, addItemBody)
        itemRepository.sellItem(multipartBody)
                .observeForever {
                    Timber.e(it!!.resourceState.toString())
                }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Timber.e("stop job service")
        return false
    }

    override fun onDestroy() {
        super.onDestroy();
        Timber.e(" service destroy")
    }

    private fun createMultipleImagePart(listImagePath: List<String>?, itemBody: AddItemBody): MultipartBody {
        val multilPartBuilder = MultipartBody.Builder()
        listImagePath?.let { list ->
            list.forEach { path ->
                val file = File(path)
                var mimeType: String? = null
                val uri = Uri.fromFile(file)
                if (Uri.fromFile(file).getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    val cr = contentResolver
                    mimeType = cr.getType(uri)
                } else {
                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                            .toString())
                    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                            fileExtension.toLowerCase())
                }
                multilPartBuilder.addFormDataPart("images"
                        , file.name
                        , RequestBody.create(MediaType.parse(mimeType), file))
            }
        }
        multilPartBuilder.addFormDataPart("name",itemBody.name)
        multilPartBuilder.addFormDataPart("price", itemBody.price.toString())
        multilPartBuilder.addFormDataPart("description",itemBody.description)
        val needToSell = if (itemBody.needToSell)  1 else 0
        multilPartBuilder.addFormDataPart("needToSell",needToSell.toString())
        multilPartBuilder.addFormDataPart("categoryID",itemBody.categoryID.toString())
        multilPartBuilder.addFormDataPart("districtID",itemBody.districtID.toString())
        multilPartBuilder.addFormDataPart("address",itemBody.address)
        multilPartBuilder.setType(MultipartBody.FORM)

        return multilPartBuilder.build()
    }
}