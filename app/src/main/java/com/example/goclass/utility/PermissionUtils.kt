package com.example.goclass.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtils(private val context: Context) {
    fun requestLocationPermissions(): Boolean {
        var permissionGranted = true

        // Check location permission; if not granted, request permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (context is ActivityCompat.OnRequestPermissionsResultCallback) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1000,
                )
            }
            permissionGranted = false
        }
        return permissionGranted
    }

    fun requestBluetoothPermissions(activity: Activity, requestCode: Int): Boolean {
        val bluetoothPermission = Manifest.permission.BLUETOOTH

        // Check bluetooth permission; if not granted, request permission
        return if (ContextCompat.checkSelfPermission(activity, bluetoothPermission) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(bluetoothPermission), requestCode)
            false
        }
    }
}
