package com.example.androidroom

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class Location {

    @RequiresApi(Build.VERSION_CODES.R)
   private fun getLocation(callback : CallBack, con : Context, longitud : Double, latitud : Double){
        val locationManager: LocationManager = con.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                con,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                con,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.getCurrentLocation(
                LocationManager.GPS_PROVIDER,
                null, con.mainExecutor,
                {
                    val result = calculateDistance(it.longitude, it.latitude, longitud, latitud)
                    callback.onSuccess(result)
                })
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun get(con: Context, longitud : Double, latitud : Double) : Float{
        getLocation(object : CallBack {
            override fun onSuccess(result: Float) {
                val distance = result
            }
        }, con, longitud, latitud)
        return 0.0F
    }

    private fun calculateDistance(longitude : Double, latitude : Double, longitud : Double, latitud : Double): Float {
        val locationA = Location("point A")
        val locationB = Location("point B")

        locationA.longitude = longitude
        locationA.latitude = latitude
        locationB.longitude = longitud
        locationB.latitude = latitud

        return locationA.distanceTo(locationB)
    }
}