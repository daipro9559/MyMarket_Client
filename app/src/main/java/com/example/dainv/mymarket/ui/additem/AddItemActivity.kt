package com.example.dainv.mymarket.ui.additem

import android.Manifest
import android.annotation.SuppressLint
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
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.entity.*
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailViewModel
import com.example.dainv.mymarket.ui.map.MapActivity
import com.example.dainv.mymarket.util.Util
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import timber.log.Timber
import java.io.File
import java.util.ArrayList
import javax.inject.Inject

class AddItemActivity : BaseActivity() {

    companion object {
        // add item_view_pager for stand
        const val STAND_KEY = "stand id"
        const val ITEM_ID_KEY = "item_view_pager id key"
        const val ADDRESS_ID = "address_id"
        const val REQUEST_TAKE_PHOTO = 1
        const val REQUEST_PICk_PHOTO = 2
        const val REQUEST_SELECT_LOCATION = 3
        const val LATITUDE_KEY = "latitude key"
        const val LONGITUDE_KEY = "longitude key"
        const val ACTION_EDIT_ITEM = "action edit item_view_pager"
    }

    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1
    private val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    private val READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    @Inject
    lateinit var imageAdapter: Lazy<ImageSelectedAdapter>
    private lateinit var mCurrentImagePath: String
    private lateinit var addItemViewModel: AddItemViewModel
    private lateinit var itemDetailViewModel: ItemDetailViewModel
    private var provinceSelect: Province? = null
    // param item_view_pager body
    private var categorySelect: Category? = null
    private var districtSelect: District? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var stand: Stand? = null
    private var isEditItemAction = false
    private lateinit var itemEdit:Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        setSupportActionBar(toolBar)
        enableHomeBack()
        getDataFromIntent()
        addItemViewModel = ViewModelProviders.of(this, viewModelFactory)[AddItemViewModel::class.java]
        initView()
        viewObserve()
        if (isEditItemAction) {
            setTitle(R.string.edit_item)
            initEditAction()
        }else{
            setTitle(R.string.post_item)
        }
    }

    private fun initEditAction() {
        // use current Address is false because location will load by item_view_pager
        btnSell.text = getString(R.string.save_edit)
        useAddressCurrent.isChecked = false
        checkboxDeleteOldImage.visibility = View.VISIBLE
        itemDetailViewModel = ViewModelProviders.of(this, viewModelFactory)[ItemDetailViewModel::class.java]
        if (intent.hasExtra(ITEM_ID_KEY)) {
            itemDetailViewModel.getItemDetail(intent!!.getStringExtra(ITEM_ID_KEY))
            itemDetailViewModel.itemDetailResult.observe(this, Observer {
                it?.r?.let {
                    itemEdit = it
                    bindItem(it)
                }
            })
        }else{
            finish()
        }
    }

    private fun bindItem(item: Item?) {
        item?.let {
            edtName.setText(it.name)
            edtPrice.setText(it.price.toString())
            edtDescription.setText(it.description)
            if (it.needToSell) {
                radioNeedToSell.isChecked = true
            } else {
                radioNeedToBuy.isChecked = true
            }
            categorySelect = it.Category!!
            txtCategory.text = it.Category?.categoryName
            edtAddress.setText(it.Address?.address)
            it.Address?.District?.let {
                txtProvince.text = it.Province?.provinceName
                txtDistrict.text = it.districtName
                districtSelect = District(it.districtID, it.districtName, it.provinceID)
                provinceSelect = Province(it.Province!!.provinceID, it.Province!!.provinceName)
            }
            latitude = it.Address?.latitude
            longitude = it.Address?.longitude
        }

    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(STAND_KEY)) {
            stand = intent.getParcelableExtra(STAND_KEY)
        }
        if (ACTION_EDIT_ITEM == intent.action) {
            isEditItemAction = true
        }
    }

    private fun viewObserve() {
        addItemViewModel.addItemResult.observe(this, Observer {
            Timber.e(it!!.resourceState.toString())
            it!!.r?.let {
                Timber.e(it.message)
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

    }

    @SuppressLint("MissingPermission")
    private fun initView() {
        // add item_view_pager for stand
        if (stand != null) {
            btnSell.setText(R.string.add_item_to_stand)
            cardDistrict.visibility = View.GONE
            cardProvince.visibility = View.GONE
            edtAddress.visibility = View.GONE
            titleAddAddress.visibility = View.GONE
            radioGroupNeedToSale.visibility = View.GONE
            titleNeedToSale.visibility = View.GONE
            cardCategory.visibility = View.GONE
            setTitle(R.string.title_add_item_to_stand)
        } else {
            setTitle(R.string.post_item)
        }
        txtConvertPrice.text = Util.convertPriceToText(0, applicationContext)
        cardDistrict.isEnabled = false
        recyclerViewImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewImage.adapter = imageAdapter.get()
        imageAdapter.get().chooseObserver.observe(this, Observer {
            //            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS,this)
            showDialogSelectPickPhoto()
        })
        btnSell.setOnClickListener {
            submitPostItem()
        }
        cardCategory.setOnClickListener {
            addItemViewModel.getAllCategory().observe(this, Observer {
                if (it!!.resourceState == ResourceState.SUCCESS) {
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
                    dialogSelectProvince.show(supportFragmentManager, DialogSelectProvince.TAG)
                }
            })
        }
        cardDistrict.setOnClickListener {
            addItemViewModel.getDistricts(provinceSelect?.provinceID)
        }
        edtPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    txtConvertPrice.text = Util.convertPriceToText(s.toString().toLong(), applicationContext)
                }
            }
        })
        selectOnMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.action = MapActivity.ACTION_SELECT_POSITION
            startActivityForResult(intent, REQUEST_SELECT_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun submitPostItem() {
        if (!checkDataInput()) {
            return
        }

        val addItemBodyBuilder: AddItemBody.Builder
        var categoryID: Int
        if (stand != null) {
            categoryID = stand!!.categoryID
            addItemBodyBuilder = AddItemBody.Builder()
                    .setName(edtName.text.toString())
                    .setPrice(edtPrice.text.toString().toInt())
                    .setNeedToSell(true)
                    .setCategoryID(categoryID)
                    .setAddressID(stand!!.Address!!.addressID)
                    .setStandId(stand!!.standID)
                    .setDescription(edtDescription.text.toString())
        } else {
            var locationManager: LocationManager? = null
            if (useAddressCurrent.isChecked) {
                if (hasNoPermission()) {
                    requestPermissions()
                    return
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
            addItemBodyBuilder = AddItemBody.Builder()
                    .setAddress(edtAddress.text.toString())
                    .setName(edtName.text.toString())
                    .setPrice(edtPrice.text.toString().toInt())
                    .setNeedToSell(radioNeedToSell.isChecked)
                    .setCategoryID(categorySelect!!.categoryID)
                    .setDescription(edtDescription.text.toString())
                    .setDistrictID(districtSelect!!.districtID)
                    .setProvinceID(provinceSelect!!.provinceID)
                    .setLatitude(latitude!!)
                    .setLongitude(longitude!!)
        }
        if (isEditItemAction){
            addItemBodyBuilder.setItemId(intent.getStringExtra(ITEM_ID_KEY))
            addItemBodyBuilder.setIsDeleteImage(checkboxDeleteOldImage.isChecked)
            addItemBodyBuilder.setAddressID(itemEdit.addressID)
        }
        addItemViewModel.postItem(addItemBodyBuilder.build(), imageAdapter.get().getItems())
        Toast.makeText(applicationContext, R.string.upload_item, Toast.LENGTH_LONG).show()
        finish()
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
            val dialogSelect = supportFragmentManager.findFragmentByTag(DialogMethodAddPhoto.TAG)
            if (dialogSelect != null && dialogSelect is DialogMethodAddPhoto) {
                dialogSelect.dismiss()
            }
            if (requestCode == REQUEST_TAKE_PHOTO) {
                imageAdapter.get().addItemToFirst(mCurrentImagePath)
                galleryAddPic(mCurrentImagePath)
            } else if (requestCode == REQUEST_PICk_PHOTO) {
                imageAdapter.get().addItemToFirst(Util.getRealPathFromURI(this, data!!.data))
            } else if (requestCode == REQUEST_SELECT_LOCATION) {
                useAddressCurrent.isChecked = false
                latitude = data!!.getDoubleExtra(LATITUDE_KEY, 0.0)
                longitude = data!!.getDoubleExtra(LONGITUDE_KEY, 0.0)
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

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            } else {
                val showRationale1 = ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA_PERMISSION)
                val showRationale2 = ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE_PERMISSION)
                val showRationale3 = ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE_PERMISSION)
                if (showRationale1 && showRationale2 && showRationale3) {
                } else {

                }
            }
            MapActivity.REQUEST_PERMISSIONS_LOCATION_CODE -> {
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
                submitPostItem()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }

    //check for camera and storage access permissions
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
        val dialogMethodAddPhoto = DialogMethodAddPhoto.newsInstance(getString(R.string.add_photo_item))
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

    private fun checkDataInput(): Boolean {
        var isPass = true
        if (edtName.text.isNullOrEmpty()) {
            edtName.error = getString(R.string.not_empty)
            isPass = false
        }
        if (edtDescription.text.isNullOrEmpty()) {
            edtDescription.error = getString(R.string.not_empty)
            isPass = false
        }
        if (edtAddress.text.isNullOrEmpty()) {
            edtAddress.error = getString(R.string.not_empty)
            isPass = false
        }
        if (categorySelect == null) {
            txtCategory.error = getString(R.string.not_empty)
            isPass = false
        }
        if(stand ==null) {
            if (districtSelect == null) {
                txtDistrict.error = getString(R.string.not_empty)
                isPass = false
            }
            if (provinceSelect == null) {
                txtProvince.error = getString(R.string.not_empty)
                isPass = false
            }

            if(!useAddressCurrent.isChecked && latitude ==null ){
                isPass = false
                Toast.makeText(applicationContext,R.string.input_coordinate,Toast.LENGTH_LONG).show()
            }
        }

        return isPass
    }

    private fun hasNoPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), MapActivity.REQUEST_PERMISSIONS_LOCATION_CODE)
    }

}