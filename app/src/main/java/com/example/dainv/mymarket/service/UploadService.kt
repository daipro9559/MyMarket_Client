package com.example.dainv.mymarket.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.*
import android.net.Uri
import android.os.PersistableBundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
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
import javax.inject.Inject
import kotlin.collections.ArrayList
import android.webkit.MimeTypeMap
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.model.ResourceState
import com.example.dainv.mymarket.util.ImageHelper
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext


class UploadService : JobService() {
    val NOTIFY_UPLOAD_CODE = 101
    val CHANNEL_ID = "upload chanel"

    @Inject
    lateinit var itemRepository: ItemRepository

    @Inject
    lateinit var gson: Gson

    lateinit var notifyBuilder: NotificationCompat.Builder

    private  var jobParams : JobParameters? =null

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
        this.jobParams = params
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
        async(UI) {
            val jobCreateMultipleImagePart = withContext(CommonPool) {
                createMultipleImagePart(imagesPath, addItemBody)
            }
            async (UI){
                buildNotificationStartUpload()
            }
            itemRepository.sellItem(jobCreateMultipleImagePart)
                    .observeForever {
                        Timber.e(it!!.resourceState.toString())
                        if (it.resourceState == ResourceState.SUCCESS){
                            buildNotificationCompleted(it.r!!.data.itemID)
                            jobFinished(jobParams,false)
                        }else if (it.resourceState == ResourceState.ERROR){
                            jobFinished(jobParams,true)
                        }
                    }
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
                var file = File(path)
                if (file.exists()) {
                    file = ImageHelper.reduceImageSize(file, 1080)
                    var mimeType: String? = null
                    val uri = Uri.fromFile(file)
                    mimeType = if (Uri.fromFile(file).scheme == ContentResolver.SCHEME_CONTENT) {
                        val cr = contentResolver
                        cr.getType(uri)
                    } else {
                        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                .toString())
                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                fileExtension.toLowerCase())
                    }
                    multilPartBuilder.addFormDataPart("images"
                            , file.name
                            , RequestBody.create(MediaType.parse(mimeType), file))
                }
            }
        }
        multilPartBuilder.addFormDataPart("name", itemBody.name)
        multilPartBuilder.addFormDataPart("price", itemBody.price.toString())
        multilPartBuilder.addFormDataPart("description", itemBody.description)
        multilPartBuilder.addFormDataPart("needToSell", itemBody.needToSell.toString())
        multilPartBuilder.addFormDataPart("categoryID", itemBody.categoryID.toString())
        multilPartBuilder.addFormDataPart("districtID", itemBody.districtID.toString())
        multilPartBuilder.addFormDataPart("address", itemBody.address)
        multilPartBuilder.setType(MultipartBody.FORM)
        return multilPartBuilder.build()
    }

    private fun buildNotificationStartUpload(){
        notifyBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(getString(R.string.upload_item))
                .setSmallIcon(R.drawable.ic_upload)
                .setProgress(0,0,true)
                .setColor(ContextCompat.getColor(this,R.color.colorButtonLogin))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        NotificationManagerCompat.from(this)
                .notify(NOTIFY_UPLOAD_CODE,notifyBuilder.build())
   }
    private fun buildNotificationCompleted(itemID:Long){
        notifyBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(getString(R.string.upload_item_completed))
                .setSmallIcon(R.drawable.ic_upload)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        NotificationManagerCompat.from(this)
                .notify(NOTIFY_UPLOAD_CODE,notifyBuilder.build())
    }
}