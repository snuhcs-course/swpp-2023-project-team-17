package com.example.goclass

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

class LocationService : Service() {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate() {
        super.onCreate()
        Log.d("Debug", "created LocationService")

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Handle new location updates here
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("LocationService", "Latitude: $latitude, Longitude: $longitude")
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Check if the permission is already granted
        Log.i("LocationService", "Received start id $startId: $intent");
        Log.d("Debug", "onStartCommand")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted; TODO: show a notification
            return START_NOT_STICKY
        }

        // Permission is granted
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10000,
            0f,
            locationListener
        )

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop location updates when the service is destroyed
        locationManager.removeUpdates(locationListener)
    }
}