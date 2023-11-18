package com.example.goclass.ui.classui.attendances

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.goclass.R
import com.example.goclass.ui.mainui.MainActivity

class LocationService : Service() {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate() {
        super.onCreate()
        Log.d("Debug", "created LocationService")

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener =
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    // Handle new location updates here
                    val latitude = location.latitude.toString()
                    val longitude = location.longitude.toString()
                    Log.d("LocationService", "Latitude: $latitude, Longitude: $longitude")

                    // Broadcast location
                    val intent = Intent("LocationUpdate")
                    intent.putExtra("latitude", latitude)
                    intent.putExtra("longitude", longitude)
                    sendBroadcast(intent)
                }

                override fun onStatusChanged(
                    provider: String?,
                    status: Int,
                    extras: Bundle?,
                ) {
                }

                override fun onProviderEnabled(provider: String) {
                }

                override fun onProviderDisabled(provider: String) {
                }
            }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        // Check if the permission is already granted
        Log.i("LocationService", "Received start id $startId: $intent")
        Log.d("Debug", "onStartCommand")

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted; TODO: show a notification
            return START_NOT_STICKY
        }
        startForegroundNotification()

        // Permission is granted
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            0f,
            locationListener,
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
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
    }

    private fun startForegroundNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntentFlags =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                pendingIntentFlags,
            )

        val notification =
            NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setContentTitle("위치 추적 중")
                .setContentText("백그라운드에서 위치를 추적 중입니다.")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.baseline_location_on_24)
                .build()

        Log.d("notificationl", "create notification")

        startForeground(1, notification)
    }
}
