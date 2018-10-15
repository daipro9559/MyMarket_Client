package com.example.dainv.mymarket.ui.additem

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.util.Util
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.app_bar_layout.view.*
import java.io.File
import java.io.IOException
import javax.inject.Inject
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.os.Build
import android.app.Activity
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.support.annotation.NonNull
import android.content.DialogInterface
import kotlinx.android.synthetic.main.app_bar_layout.*


const val REQUEST_TAKE_PHOTO = 1

class AddItemActivity : BaseActivity() {
    @Inject
    lateinit var router: AddItemActivityRouter
    @Inject
    lateinit var imageAdapter: Lazy<ImageSelectedAdapter>
    private lateinit var mCurrentImagePath :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        setSupportActionBar(toolBar)
        setTitle(R.string.post_item)
        enableHomeBack()
        initView()
    }

    private fun initView() {
        recyclerViewImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewImage.adapter = imageAdapter.get()
        imageAdapter.get().chooseObserver.observe(this, Observer {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS,this)
        })
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    Util.createImageFile(this)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    mCurrentImagePath = it.absolutePath
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            imageAdapter.get().addItemToFirst(mCurrentImagePath)
            galleryAddPic()
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(mCurrentImagePath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1
    val CAMERA_PERMISSION = android.Manifest.permission.CAMERA

    val READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE

    val WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            } else {
                val showRationale1 = shouldShowRequestPermissionRationale(CAMERA_PERMISSION)
                val showRationale2 = shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE_PERMISSION)
                val showRationale3 = shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE_PERMISSION)
                if (showRationale1 && showRationale2 && showRationale3) {
                    //explain to user why we need the permissions
//                    mDialogType = ValueConstants.DialogType.DIALOG_DENY
                    // Show dialog with
//                    openAlertDialog(mRequestPermissions, mGrantPermissions, mCancel, this, this@MyActivity)
                } else {
                    //explain to user why we need the permissions and ask him to go to settings to enable it
//                    mDialogType = ValueConstants.DialogType.DIALOG_NEVER_ASK
//                    openAlertDialog(mRequsetSettings, mGoToSettings, mCancel, this, this@MyActivity)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private fun checkMultiplePermissions(permissionCode: Int, context: Context) {

        val permissions = arrayOf(CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION)
        if (!hasPermissions(context, *permissions)) {
            ActivityCompat.requestPermissions(context as Activity, permissions, permissionCode)
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    fun openAlertDialog(message: String, positiveBtnText: String, negativeBtnText: String,
                        listener: OnDialogButtonClickListener, mContext: Context) {

        val builder = AlertDialog.Builder(mContext)
        builder.setPositiveButton(positiveBtnText, DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
//            listener.onPositiveButtonClicked()
        })
        builder.setPositiveButton(positiveBtnText, DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
//            listener.onNegativeButtonClicked()
        })

        builder.setTitle(mContext.getResources().getString(R.string.app_name))
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setCancelable(false)
        builder.create().show()
    }
}