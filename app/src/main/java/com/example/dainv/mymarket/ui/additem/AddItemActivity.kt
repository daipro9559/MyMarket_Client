package com.example.dainv.mymarket.ui.additem

import android.annotation.TargetApi
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.base.BaseActivity
import com.example.dainv.mymarket.model.*
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.util.Util
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import timber.log.Timber
import java.io.File
import java.util.ArrayList
import javax.inject.Inject


const val REQUEST_TAKE_PHOTO = 1
const val REQUEST_PICk_PHOTO = 2

class AddItemActivity : BaseActivity() {
    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1
    private val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    private val READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    @Inject
    lateinit var router: AddItemActivityRouter
    @Inject
    lateinit var imageAdapter: Lazy<ImageSelectedAdapter>
    private lateinit var mCurrentImagePath: String
    lateinit var addItemViewModel: AddItemViewModel
    private lateinit var provinceSelect:Province
    // param item body
    private lateinit var categorySelect: Category
    private lateinit var districtSelect: District

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        setSupportActionBar(toolBar)
        setTitle(R.string.post_item)
        enableHomeBack()
        addItemViewModel = ViewModelProviders.of(this, viewModelFactory)[AddItemViewModel::class.java]
        initView()
        viewObserve()
    }

    private fun viewObserve() {
        addItemViewModel.addItemResult.observe(this, Observer {
            Timber.e(it!!.resourceState.toString())
            it!!.r?.let {
                Timber.e(it.message)
            }
        })
    }

    private fun initView() {
        txtConvertPrice.text = Util.convertPriceToFormat(0)
        cardDistrict.isEnabled = false
        recyclerViewImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewImage.adapter = imageAdapter.get()
        imageAdapter.get().chooseObserver.observe(this, Observer {
            //            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS,this)
            showDialogSelectPickPhoto()
        })
        cardProvince.setOnClickListener {
        }
        btnSell.setOnClickListener {
            val addItemBody = AddItemBody.Builder()
                    .setAddress(edtAddress.text.toString())
                    .setName(edtName.text.toString())
                    .setPrice(edtPrice.text.toString().toInt())
                    .setNeedToSell(radioNeedToSell.isChecked)
                    .setCategoryID(categorySelect.categoryID)
                    .setDescription(edtDescription.text.toString())
                    .setDistrictID(districtSelect.districtID)
                    .build()
            addItemViewModel.sellItem(addItemBody, imageAdapter.get().getItems())
            Toast.makeText(applicationContext,R.string.upload_item,Toast.LENGTH_LONG).show()
            finish()
        }
        cardCategory.setOnClickListener {
            addItemViewModel.getAllCategory().observe(this, Observer {
                if (it!!.resourceState == ResourceState.SUCCESS){
                    val dialogSelectCategory = DialogSelectCategory.newInstance(it.r as ArrayList<Category>)
                    dialogSelectCategory.callback = {
                        categorySelect = it
                        txtCategory.text = it.categoryName
                    }
                    dialogSelectCategory.show(supportFragmentManager, DialogSelectCategory.TAG)
                }
            })

        }
        cardProvince.setOnClickListener {
            addItemViewModel.getAllProvince().observe(this, Observer {
                it!!.r?.let {
                    val dialogSelectProvince = DialogSelectProvince.newInstance(it)
                    dialogSelectProvince.callback = {
                        provinceSelect = it
                        txtProvince.text = it.provinceName
                        cardDistrict.isEnabled = true
                    }
                    dialogSelectProvince.show(supportFragmentManager,DialogSelectProvince.TAG)
                }
            })

        }
        cardDistrict.setOnClickListener {
            addItemViewModel.getDistricts(provinceSelect.provinceID)
            addItemViewModel.districtLiveData.observe(this, Observer {
                if(it!!.resourceState == ResourceState.SUCCESS){
                    val dialogSelectDistrict = DialogSelectDistrict.newInstance(it.r!!)
                    dialogSelectDistrict.callback = {
                        districtSelect = it
                        txtDistrict.text = it.districtName
                    }
                    dialogSelectDistrict.show(supportFragmentManager,DialogSelectDistrict.TAG)
                }
            })

        }
        edtPrice.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    txtConvertPrice.text = Util.convertPriceToFormat(s.toString().toLong())
                }
            }

        })
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    Util.createImageFile(this)
                } catch (ex: Throwable) {
                    // ErrorResponse occurred while creating the File
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
        if (resultCode == Activity.RESULT_OK) {
            val dialogSelect = supportFragmentManager.findFragmentByTag(DialogMethodAddPhoto.TAG) as DialogMethodAddPhoto
            if (dialogSelect != null && dialogSelect.dialog.isShowing) {
                dialogSelect.dismiss()
            }
            if (requestCode == REQUEST_TAKE_PHOTO) {
                imageAdapter.get().addItemToFirst(mCurrentImagePath)
                galleryAddPic()
            } else if (requestCode == REQUEST_PICk_PHOTO) {
                imageAdapter.get().addItemToFirst(getRealPathFromURI(this, data!!.data))
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(mCurrentImagePath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            } else {
                val showRationale1 = shouldShowRequestPermissionRationale(CAMERA_PERMISSION)
                val showRationale2 = shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE_PERMISSION)
                val showRationale3 = shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE_PERMISSION)
                if (showRationale1 && showRationale2 && showRationale3) {
                } else {

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

    //
    private fun showDialogSelectPickPhoto() {
        val dialogMethodAddPhoto = DialogMethodAddPhoto.newsInstance()
        dialogMethodAddPhoto.clickCallback = {
            if (it == R.id.actionOne) {
                checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, this)
            } else if (it == R.id.actionTwo) {
                //TODO need check permission read storage
                val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_PICk_PHOTO)
            }
        }
        dialogMethodAddPhoto.show(supportFragmentManager, DialogMethodAddPhoto.TAG)
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
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
}