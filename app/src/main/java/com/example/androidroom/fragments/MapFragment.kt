package com.example.androidroom.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidroom.R
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener




class MapFragment : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val sharedPreferencesManager = SharedPreferencesManager()
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_fragment)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createMapFragment()
    }

    //Mapa Fragment Ciudades
    private fun createMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //ZOOM al lugar marcado
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isMyLocationEnabled = true

        fusedLocationClient?.lastLocation?.addOnSuccessListener(this,
            OnSuccessListener<Any?> { location ->
                if (location != null) {

                }
            })
        val longitude = sharedPreferencesManager.getLongitude(this).toString().toDouble()
        val latitude = sharedPreferencesManager.getLatitude(this).toString().toDouble()

        val restaurant = LatLng(latitude , longitude)
        map.addMarker(MarkerOptions().position(restaurant).title(intent.getStringExtra("name")))

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(restaurant, 18f),4000,null)
    }
}