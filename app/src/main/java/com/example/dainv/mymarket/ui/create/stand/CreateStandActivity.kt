package com.example.dainv.mymarket.ui.create.stand

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.glide.GlideApp
import com.example.dainv.mymarket.entity.Category
import com.example.dainv.mymarket.entity.District
import com.example.dainv.mymarket.entity.Province
import com.example.dainv.mymarket.entity.ResourceState
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import com.example.dainv.mymarket.ui.additem.AddItemViewModel
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.ui.map.MapActivity
import com.example.dainv.mymarket.util.Util
import kotlinx.android.synthetic.main.activity_create_stand.*
import kotlinx.android.synthetic.main.app_bar_layout.view.*
import java.io.File
import java.util.ArrayList

class CreateStandActivity : BaseActivity() {

    companion object {
        const val REQUEST_SELECT_LOCATION = 3
        const val REQUEST_TAKE_PHOTO = 1
        const val REQUEST_PICk_PHOTO = 2
        const val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1
        const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
        const val READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
        const val WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    }


    private var imagePath: String? = null

    lateinit var createStandViewModel: CreateStandViewModel
    lateinit var addItemViewModel: AddItemViewModel
    private lateinit var districtSelect: District
    private lateinit var provinceSelect: Province
    private lateinit var categorySelect: Category
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_stand)
        setSupportActionBar(appBarLayout.toolBar)
        title = getString(R.string.create_stand_new)
        enableHomeBack()
        createStandViewModel = ViewModelProviders.of(this, viewModelFactory)[CreateStandViewModel::class.java]
        addItemViewModel = ViewModelProviders.of(this, viewModelFactory)[AddItemViewModel::class.java]
        initView()
        createStandViewModel.createResult.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
            }
            it!!.r?.let { it ->
                if (it) {
                    Toast.makeText(applicationContext, getString(R.string.create_stand_completed), Toast.LENGTH_LONG).show()
                    setResult(Activity.RESULT_OK)
                }
            }
        })

        addItemViewModel.districtLiveData.observe(this, Observer {
            if (it!!.resourceState == ResourceState.SUCCESS) {
                val dialogSelectDistrict = DialogSelectDistrict.newInstance(it.r!!)
                dialogSelectDistrict.callback = {
                    districtSelect = it
                    txtDistrict.text = it.districtName
                }
                dialogSelectDistrict.show(supportFragmentManager, DialogSelectDistrict.TAG)
            }
        })
        cardProvince.setOnClickListener {
            addItemViewModel.getAllProvince().observe(this, Observer {
                it!!.r?.let {
                    val dialogSelectProvince = DialogSelectProvince.newInstance(it)
                    dialogSelectProvince.callback = {
                        provinceSelect = it
                        txtProvince.text = it.provinceName
                        cardDistrict.isEnabled = true
                    }
                    dialogSelectProvince.show(supportFragmentManager, DialogSelectProvince.TAG)
                }
            })
        }
        cardDistrict.setOnClickListener {
            addItemViewModel.getDistricts(provinceSelect.provinceID)
        }

        cardCategory.setOnClickListener {
            addItemViewModel.getAllCategory().observe(this, Observer {
                if (it!!.resourceState == ResourceState.SUCCESS) {
                    val dialogSelectCategory = DialogSelectCategory.newInstance(it.r as ArrayList<Category>)
                    dialogSelectCategory.callback = { category ->
                        categorySelect = category
                        txtCategory.text = category.categoryName
                    }
                    dialogSelectCategory.show(supportFragmentManager, DialogSelectCategory.TAG)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, this)
    }

    @SuppressLint("MissingPermission")
    private fun initView() {
        selectOnMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.action = MapActivity.ACTION_SELECT_POSITION
            startActivityForResult(intent, REQUEST_SELECT_LOCATION)
        }
        cardViewSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_PICk_PHOTO)
        }
        cardViewTakeImage.setOnClickListener {
            dispatchTakePictureIntent()
        }
        btnCreate.setOnClickListener {
            if (edtName.text.isNullOrEmpty()
                    || edtDescription.text.isNullOrEmpty()
                    || edtAddress.text.isNullOrEmpty()
                    || districtSelect == null
                    || categorySelect == null) {
                Toast.makeText(applicationContext, R.string.please_input_full_information, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var locationManager: LocationManager? = null
            if (useAddressCurrent.isChecked) {
                if (hasNoPermission()) {
                    requestPermissions()
                    return@setOnClickListener
                }
                var location: Location? = null
                if (locationManager == null) {
                    locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                }
                if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                } else if (locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
                latitude = location?.latitude
                longitude = location?.longitude
            }
            createStandViewModel.createStand(edtName.text.toString()
                    , edtDescription.text.toString(),
                    imagePath,
                    edtAddress.text.toString(),
                    districtSelect.districtID,
                    categorySelect.categoryID,
                    latitude,
                    longitude)
        }
    }

    private fun hasNoPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), MapActivity.REQUEST_PERMISSIONS_LOCATION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            } else {
                val showRationale1 = ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA_PERMISSION)
                val showRationale2 = ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE_PERMISSION)
                val showRationale3 = ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE_PERMISSION)
                if (showRationale1 && showRationale2 && showRationale3) {
                } else {

                }
            }

        } else if (requestCode == MapActivity.REQUEST_PERMISSIONS_LOCATION_CODE) {
            for (i in 0 until grantResults.size) {
                if (grantResults[i] === PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        Toast.makeText(this, "you need go to the setting and enable all permission location : ", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }

                }
                if (grantResults[i] !== PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "no have Permission : " + permissions[i], Toast.LENGTH_LONG).show()
                    finish()
                    return
                }
            }
        }

    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private fun checkMultiplePermissions(permissionCode: Int, context: Context) {
        val permissions = arrayOf(CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION)
        if (!hasPermissions(context, *permissions)) {
            ActivityCompat.requestPermissions(context as Activity, permissions, permissionCode)
        } else {

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_TAKE_PHOTO -> {
                    galleryAddPic(imagePath!!)
                    showImage()
                }
                REQUEST_PICk_PHOTO -> {
                    imagePath = Util.getRealPathFromURI(this, data!!.data)
                    showImage()
                }
                REQUEST_SELECT_LOCATION -> {
                    useAddressCurrent.isChecked = false
                    latitude = data!!.getDoubleExtra(AddItemActivity.LATITUDE_KEY, 0.0)
                    longitude = data!!.getDoubleExtra(AddItemActivity.LONGITUDE_KEY, 0.0)
                }
            }
        }
    }

    private fun galleryAddPic(path: String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(path)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
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
                photoFile?.also { it ->
                    imagePath = it.absolutePath
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

    private fun showImage() {
        GlideApp.with(imageSelect.context)
                .load(imagePath)
                .into(DrawableImageViewTarget(imageSelect))
                .waitForLayout()
    }

    private fun checkDataInput(): Boolean {
        var isPass = true
        return isPass
    }
}