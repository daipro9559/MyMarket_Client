package com.example.dainv.mymarket.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.design.card.MaterialCardView
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.ListPopupWindow
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseActivity
import com.example.dainv.mymarket.util.Util
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_maps.*
import android.widget.Toast
import com.example.dainv.mymarket.entity.*
import com.example.dainv.mymarket.ui.ImagePreviewActivity
import com.example.dainv.mymarket.ui.additem.AddItemActivity
import com.example.dainv.mymarket.ui.dialog.DialogSelectCategory
import com.example.dainv.mymarket.ui.dialog.DialogSelectDistrict
import com.example.dainv.mymarket.ui.dialog.DialogSelectProvince
import com.example.dainv.mymarket.ui.itemdetail.ItemDetailActivity
import com.example.dainv.mymarket.ui.items.ListItemViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.bottom_sheet_filter_map.*
import kotlinx.android.synthetic.main.bottom_sheet_item_map.*
import timber.log.Timber
import javax.inject.Inject


class MapActivity : BaseActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    companion object {
        const val ACTION_SELECT_POSITION = "select position by map"
        const val REQUEST_PERMISSIONS_LOCATION_CODE = 150
        const val CATEGORY_KEY = "category key"
        const val PROVINCE_KEY = "province  key"
        const val DISTRICT_KEY = "district key"
    }

    private var action: String? = null
    private var isActionSelect = false
    // viewmodel
    private lateinit var mapViewModel: MapViewModel
    private lateinit var listItemViewModel: ListItemViewModel

    private  var categorySelect: Category? =null
    private var districtSelect: District? = null
    private var provinceSelected: Province? = null
    private val bearOfCompass: Float = 0.toFloat()
    private var bearOfCamera: Float = 0.toFloat()
    private var popupMenu: ListPopupWindow? = null
    private var menuAdapter: MenuAdapter? = null
    private lateinit var googleMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var myCoordinate: LatLng? = null
    private var myLocationMarker: Marker? = null
    private var myLocation: Location? = null
    private var circleRadius: Circle? = null
    @Inject
    lateinit var bearing: Bearing
    private var isMapReady: Boolean = false

    //google location param
    private var client: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var googleApiClient: GoogleApiClient? = null

    //old value of  camera
    private var oldBearingOffCamera: Float = 0.toFloat()
    private var oldBearingOffCompass: Float = 0.toFloat()
    private lateinit var bottomSheetBehaviorFilter: BottomSheetBehavior<MaterialCardView>
    private lateinit var bottomSheetBehaviorItem: BottomSheetBehavior<MaterialCardView>
    private val queryMap = HashMap<String, String?>()
    private lateinit var itemMapClick : ItemMap

    // for action select position

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        listItemViewModel = ViewModelProviders.of(this, viewModelFactory)[ListItemViewModel::class.java]
        mapViewModel = ViewModelProviders.of(this, viewModelFactory)[MapViewModel::class.java]
        isActionSelect = (ACTION_SELECT_POSITION) == intent.action
        initView()
        lifecycle.addObserver(bearing)
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        viewObserve()
        initValue()
    }


    private fun initValue() {
        //default
        edtRadius.setText("5")
        locationRequest = LocationRequest.create()
        client = LocationServices.getFusedLocationProviderClient(this)
        locationRequest?.apply {
            interval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fastestInterval = 3000
            googleApiClient = GoogleApiClient.Builder(this@MapActivity)
                    .addConnectionCallbacks(this@MapActivity)
                    .addOnConnectionFailedListener(this@MapActivity)
                    .addApi(LocationServices.API)
                    .build()
        }
        if (intent.hasExtra(CATEGORY_KEY)) {
            categorySelect = intent.getParcelableExtra(CATEGORY_KEY)
            txtCategory.text = categorySelect?.categoryName
        }

    }

    private fun initView() {
        if (isActionSelect) {
            btnConfirmLocation.visibility = View.VISIBLE
            btnFilter.hide()
            btnConfirmLocation.setOnClickListener{
                if (!isMapReady){
                    Toast.makeText(applicationContext,R.string.map_not_ready,Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val intent  = Intent(this,AddItemActivity::class.java)
                intent.putExtra(AddItemActivity.LATITUDE_KEY,googleMap.cameraPosition.target.latitude)
                intent.putExtra(AddItemActivity.LONGITUDE_KEY,googleMap.cameraPosition.target.longitude)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            markerVirtual.visibility = View.VISIBLE
        } else {
            bottomSheetBehaviorFilter = BottomSheetBehavior.from(bottomSheetFilter)
            bottomSheetBehaviorItem = BottomSheetBehavior.from(bottomSheetItem)
            bottomSheetBehaviorFilter.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                    Timber.e(" " + (-bottomSheetFilter.height + bottomSheetFilter.height * (1 - p1)))
                    btnMyLocation.animate()
                            .translationY(-bottomSheetFilter.height + bottomSheetFilter.height * (1 - p1))
                            .setDuration(0)
                            .start()
                    btnFilter.animate()
                            .translationY(-bottomSheetFilter.height + bottomSheetFilter.height * (1 - p1))
                            .setDuration(0)
                            .start()
                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                        btnMyLocation.animate()
                                .translationY(0f)
                                .setDuration(0)
                                .start()
                        btnFilter.animate()
                                .translationY(0f)
                                .setDuration(0)
                                .start()
                    } else if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
                        btnMyLocation.animate()
                                .translationY(-bottomSheetFilter.height.toFloat())
                                .setDuration(0)
                                .start()
                        btnFilter.animate()
                                .translationY(-bottomSheetFilter.height.toFloat())
                                .setDuration(0)
                                .start()
                        bottomSheetBehaviorItem.state = BottomSheetBehavior.STATE_COLLAPSED
                    } else if (p1 == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                        Timber.e("STATE_HALF_EXPANDED ")

                    }
                }
            })
            bottomSheetBehaviorItem.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                    btnMyLocation.animate()
                            .translationY(-bottomSheetItem.height + bottomSheetItem.height * (1 - p1))
                            .setDuration(0)
                            .start()
                    btnFilter.animate()
                            .translationY(-bottomSheetItem.height + bottomSheetItem.height * (1 - p1))
                            .setDuration(0)
                            .start()
                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                        btnMyLocation.animate()
                                .translationY(0f)
                                .setDuration(0)
                                .start()
                        btnFilter.animate()
                                .translationY(0f)
                                .setDuration(0)
                                .start()
                    } else if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
                        btnMyLocation.animate()
                                .translationY(-bottomSheetItem.height.toFloat())
                                .setDuration(0)
                                .start()
                        btnFilter.animate()
                                .translationY(-bottomSheetItem.height.toFloat())
                                .setDuration(0)
                                .start()
                    } else if (p1 == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                        Timber.e("STATE_HALF_EXPANDED ")

                    }
                }

            })
            seekbarRadius.max = 20
            seekbarRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    edtRadius.setText(progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })
            edtRadius.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(s.toString().isNullOrEmpty()){
                        return
                    }
                    seekbarRadius.progress = s.toString().toInt()
                }

            })

            btnFilter.setOnClickListener {
                if (bottomSheetBehaviorFilter.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehaviorFilter.setState(BottomSheetBehavior.STATE_COLLAPSED)
                } else {
                    bottomSheetBehaviorFilter.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
            }

            cardCategory.setOnClickListener {
                listItemViewModel.getAllCategory().observe(this, Observer {
                    if (it!!.resourceState == ResourceState.SUCCESS) {
                        val arrayList = ArrayList<Category>(it.r)
                        val dialoSelectCategory = DialogSelectCategory.newInstance(arrayList)
                        dialoSelectCategory.callback = {
                            categorySelect = it
                            txtCategory.text = categorySelect?.categoryName

                        }
                        dialoSelectCategory.show(supportFragmentManager, DialogSelectCategory.TAG)
                    }
                })
            }

            btnConfirmFilter.setOnClickListener {
                bottomSheetBehaviorFilter.state = BottomSheetBehavior.STATE_COLLAPSED
                findNearestItem()
            }
            cardProvinceFilter.setOnClickListener {
                listItemViewModel.getAllProvince().observe(this, Observer {
                    if (it?.resourceState == ResourceState.SUCCESS) {
                        val arrayList = ArrayList<Province>(it.r)
                        arrayList.add(0, Province(0, getString(R.string.all_province)))
                        val dialogSelectProvince = DialogSelectProvince.newInstance(arrayList)
                        dialogSelectProvince.callback = {
                            provinceFilter.text = it.provinceName
                            provinceSelected = it
                            if (provinceSelected?.provinceID != 0) {
                                titleDistrict.visibility = View.VISIBLE
                                cardDistrictFilter.visibility = View.VISIBLE
                            } else {
                                titleDistrict.visibility = View.GONE
                                cardDistrictFilter.visibility = View.GONE
                            }
                            districtFilter.text = getString(R.string.all_district)
                            districtSelect = null
                        }
                        dialogSelectProvince.show(supportFragmentManager, DialogSelectProvince.TAG)
                    }
                })
            }

            cardDistrictFilter.setOnClickListener {
                listItemViewModel.getDistrics(provinceSelected!!.provinceID).observe(this, Observer {
                    if (it?.resourceState == ResourceState.SUCCESS) {
                        val arrayList = ArrayList<District>(it.r)
                        arrayList.add(0, District(0, getString(R.string.all_district), provinceSelected!!.provinceID))
                        val dialogSelectDistrict = DialogSelectDistrict.newInstance(arrayList)
                        dialogSelectDistrict.callback = {
                            districtSelect = it
                            districtFilter.text = it.districtName

                        }
                        dialogSelectDistrict.show(supportFragmentManager, DialogSelectDistrict.TAG)
                    }
                })
            }
            needToBuy.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && needToSell.isChecked) needToSell.isChecked = false
            }
            needToSell.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && needToBuy.isChecked) needToBuy.isChecked = false
            }

            btnDetail.setOnClickListener {
                val intent = Intent(this,ItemDetailActivity::class.java)
                intent.putExtra("itemID",itemMapClick.itemID)
                intent.action = ItemDetailActivity.ACTION_SHOW_FROM_ID
                startActivityWithAnimation(intent)
            }
        }

        btnMyLocation.setOnClickListener {
            requestMyLocation()
        }
        btnMapType.setOnClickListener {
            showPopupMenuMapType()
        }

    }

    @SuppressLint("MissingPermission")
    private fun settingMap() {
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.isMyLocationEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.isBuildingsEnabled = true
        googleMap.setOnCameraMoveListener {
            bearOfCamera = googleMap.cameraPosition.bearing
            updateBearingChangeByCamera(bearOfCamera)
//            if(isActionSelect) {
//                markerSelectPosition?.apply {
//                    remove()
//                }
//                markerSelectPosition  = googleMap.addMarker(MarkerOptions().position(googleMap.cameraPosition.target))
//            }
        }
        if(!isActionSelect){
            googleMap.setOnMarkerClickListener {
                val itemMap = it.tag
                if(itemMap !=null && itemMap is ItemMap) {
                    showItemDetail(itemMap)
                     true
                }else{
                     false
                }
            }
        }else{
//            markerSelectPosition = googleMap.addMarker(MarkerOptions().position(googleMap.cameraPosition.target))
        }
    }

    private fun viewObserve() {
        bearing.bearObserve.subscribe {
            updateBearingChangeByCompass(it)
        }
        mapViewModel.listItem.observe(this, Observer {
            if (it!!.resourceState == ResourceState.LOADING) {
                loadingLayout.visibility = View.VISIBLE
            } else {
                loadingLayout.visibility = View.GONE
                if (it.resourceState == ResourceState.SUCCESS) {
                    // clear all markers
                    googleMap.clear()
                    requestMyLocationWithoutCamera()
                    circleRadius = googleMap.addCircle(CircleOptions()
                            .center(LatLng(myLocation!!.latitude, myLocation!!.longitude))
                            .radius(edtRadius.text.toString().toDouble() * 1000)
                            .strokeWidth(2f)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(50, 255, 0, 0))
                    )
                    showItemMarker(it.r?.data)
                }
            }
        })
    }

    // google client callback
    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (hasNoPermission()) {
            requestPermissions()
            return
        }
        if (locationManager == null) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        }
        if (hasNoPermission()) {
            requestPermissions()
            return
        } else {
            client!!.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    p0?.locations?.forEach {
                        it?.let { location ->
                            updateMyLocation(location)
                            Timber.e("bearing : " + location.bearing)
                        }
                    }
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    super.onLocationAvailability(p0)
                    Timber.e("location available")
                }
            }, null)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Timber.e("ConnectionSuspended")
    }

    // google client connect fail
    override fun onConnectionFailed(p0: ConnectionResult) {
        Timber.e("location Failed")
    }

    override fun onMapReady(p0: GoogleMap?) {
        isMapReady = true
        this.googleMap = p0!!
        settingMap()
        requestMyLocation()
    }

    private fun moveCamera(position: LatLng?, zoom: Int) {
        position?.let {
            val cameraPosition = CameraPosition.Builder()
                    .target(position)
                    .zoom(zoom.toFloat())
                    .build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

    }

    private fun updateBearingChangeByCompass(bearing: Float) {

        if (myLocationMarker == null) {
            return
        }
        if (bearing - oldBearingOffCamera >= 0) {
            myLocationMarker!!.rotation = bearing - oldBearingOffCamera
        } else {
            myLocationMarker!!.rotation = 360 + (bearing - oldBearingOffCamera)
        }
        oldBearingOffCompass = bearing
    }

    private fun updateBearingChangeByCamera(bearing: Float) {

        if (myLocationMarker == null) {
            return
        }
        if (bearOfCompass - bearing > 0) {
            myLocationMarker!!.rotation = bearOfCompass - bearing
        } else {
            myLocationMarker!!.rotation = 360 - bearing + bearOfCompass
        }

        oldBearingOffCamera = bearing
    }

    private fun updateMyLocation(location: Location) {
        this.myLocation = location
        if (isMapReady) {
            myCoordinate = LatLng(location.latitude, location.longitude)

            if (myLocationMarker != null) {
                myLocationMarker!!.remove()
            }
            val bitmap = Util.getBitmap(applicationContext, R.drawable.ic_dot_my_location)
            myLocationMarker = googleMap.addMarker(MarkerOptions().position(LatLng(location!!.latitude, location!!.longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)))
        }
    }

    private fun resizeMapIcons(id: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(resources, id)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    @SuppressLint("MissingPermission")
    private fun requestMyLocationWithoutCamera() {
        if (hasNoPermission()) {
            requestPermissions()
            return
        }
        if (locationManager == null) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            myLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            myLocation?.let {
                myCoordinate = LatLng(it.latitude, it.longitude)
            }
//            Timber.e(" " + myCoordinate.latitude + " -" + myCoordinate.longitude)
        } else if (locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            myLocation = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            myLocation?.let {
                myCoordinate = LatLng(it.latitude, it.longitude)
            }
        }
        myLocationMarker?.let {
            it.remove()
        }
        myLocation?.let {
            val bitmap = Util.getBitmap(applicationContext, R.drawable.ic_dot_my_location)
            myLocationMarker = googleMap.addMarker(MarkerOptions().position(LatLng(myLocation!!.latitude, myLocation!!.longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)))
        }
    }

    private fun requestMyLocation() {
        requestMyLocationWithoutCamera()
        myCoordinate?.let {
            moveCamera(myCoordinate, 17)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_LOCATION_CODE) {
            for (i in 0 until grantResults.size) {
                if (grantResults[i] === PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        Toast.makeText(this, "you need go to the setting and enable all permission : ", Toast.LENGTH_LONG).show()
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
            requestMyLocation()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        googleApiClient?.let {
            it.connect()
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        googleApiClient?.let {
            it.disconnect()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    private fun showPopupMenuMapType() {
        if (popupMenu == null) {
            popupMenu = ListPopupWindow(this)
            popupMenu!!.width = Util.convertDpToPx(200f, this)
            menuAdapter = MenuAdapter(this, R.layout.item_menu_layout)
            popupMenu!!.anchorView = btnMapType
            popupMenu!!.setOnItemClickListener { adapterView, view, i, l ->
                if (i == googleMap.mapType - 1) {
                    popupMenu!!.dismiss()

                }
                when (i) {
                    0 -> {
                        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        menuAdapter!!.changePositionSelect(0)
                    }
                    1 -> {
                        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        menuAdapter!!.changePositionSelect(1)
                    }
                    2 -> {
                        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        menuAdapter!!.changePositionSelect(2)
                    }
                }
                popupMenu!!.dismiss()
            }
            popupMenu!!.setAdapter(menuAdapter)

        }
        menuAdapter!!.changePositionSelect(googleMap.mapType - 1)
        popupMenu!!.show()

    }

    private fun hasNoPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_PERMISSIONS_LOCATION_CODE)
    }

    private fun findNearestItem() {
        myLocation?.let {
            queryMap["latitude"] = it.latitude.toString()
            queryMap["longitude"] = it.longitude.toString()
            queryMap["categoryID"] = categorySelect?.categoryID?.toString()
            if( provinceSelected != null){
                queryMap["provinceID"] = provinceSelected?.provinceID?.toString()
            }else{
                queryMap.remove("provinceID")
            }
            if(districtSelect != null){
                queryMap["districtID"] = districtSelect?.districtID?.toString()
            }else{
                queryMap.remove("districtID")
            }
            queryMap["radius"] = edtRadius.text.toString()
            if(needToBuy.isChecked || needToSell.isChecked){
                queryMap["needToSell"] = needToSell.isChecked.toString()
            }else{
                queryMap.remove("needToSell")
            }
            if (edtPriceTo.text.isNullOrEmpty()) {
                try {
                    queryMap.remove("priceMax")
                } catch (e: Exception) {
                }
            } else {
                queryMap["priceMax"] = edtPriceTo.text.toString()
            }
            if (edtPriceTo.text.isNullOrEmpty()) {
                try {
                    queryMap.remove("priceMin")
                } catch (e: Exception) {
                }
            } else {
                queryMap["priceMin"] = edtPriceFrom.text.toString()
            }
            mapViewModel.findItem(queryMap)
        }
    }

    private fun showItemMarker(items: List<ItemMap>?) {
        items?.forEach { item ->
            val latlng = LatLng(item.latitude, item.longitude)
            val marker = googleMap.addMarker(MarkerOptions().position(latlng))
            marker.tag = item
        }
    }

    private fun showItemDetail(item: ItemMap) {
        itemMapClick = item
        txtName.text = item.name
        txtPrice.text = Util.convertPriceToText(item.price, applicationContext)
        txtDescription.text = item.description
        txtTime.text = Util.convertTime(item.updatedAt, applicationContext)
        val verticalAdapter = VerticalAdapter(item.images,applicationContext)
        verticalAdapter.imageClickObserve.subscribe {
            val intent = Intent(this, ImagePreviewActivity::class.java)
            intent.putExtra(ImagePreviewActivity.IMAGE_URL_KEY,it)
            startActivityWithAnimation(intent)
        }
        verticalViewPager.adapter = verticalAdapter
        verticalViewPager.scrollDuration = 500
        bottomSheetBehaviorItem.state = BottomSheetBehavior.STATE_EXPANDED

    }
}