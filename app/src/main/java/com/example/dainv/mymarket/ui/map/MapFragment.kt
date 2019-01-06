package com.example.dainv.mymarket.ui.map

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.example.dainv.mymarket.R
import com.example.dainv.mymarket.ui.BaseFragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment :BaseFragment(), OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks{
    companion object {
        fun newInstance() : MapFragment{
            val bundle = Bundle()
            val mapFragment = MapFragment()
            mapFragment.arguments = bundle
            return mapFragment
        }
    }

    private lateinit var locationManager: LocationManager
    private lateinit var googleMap: GoogleMap
    override fun getLayoutID() = R.layout.fragment_map
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!

    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

}