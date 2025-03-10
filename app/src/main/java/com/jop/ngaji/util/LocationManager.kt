package com.jop.ngaji.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LocationManager(private val context: Context, private val fusedLocationProviderClient: FusedLocationProviderClient) {

    fun getLiveLocation(locationRequest: LocationRequest, callback: LocationCallback) {
        val hasGrantedFineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasGrantedCoarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled && !(hasGrantedCoarseLocationPermission || hasGrantedFineLocationPermission)) {
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
    }

    fun stopLiveLocation(callback: LocationCallback){
        fusedLocationProviderClient.removeLocationUpdates(callback)
    }
}