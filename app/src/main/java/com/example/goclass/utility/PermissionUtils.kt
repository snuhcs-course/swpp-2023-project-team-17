package com.example.goclass.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtils(private val context: Context) {
    fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
        }
    }

//<<<<<<< HEAD
//    fun requestBluetoothPermissions(callback: (Boolean) -> Unit) {
//        val bluetoothPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            Manifest.permission.BLUETOOTH_CONNECT
//        } else {
//            Manifest.permission.BLUETOOTH
//        }
//=======
    fun requestBluetoothPermissions() {
        val permissionsToRequest = mutableListOf<String>()
//>>>>>>> 6f270b13565b158eca00dc6c27788cffd7643903

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE)
        } else {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH)
        }
        val notGrantedPermissions = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (notGrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity, notGrantedPermissions.toTypedArray(), 101)
        }

//        callback(true)
    }

    fun requestBluetoothAdvertisePermissionsWithCallback(callback: (Boolean) -> Unit) {
        val bluetoothAdvertisePermission = Manifest.permission.BLUETOOTH_ADVERTISE

        if (ContextCompat.checkSelfPermission(
                context,
                bluetoothAdvertisePermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request Bluetooth advertising permissions
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(bluetoothAdvertisePermission),
                102
            )
        } else {
            // Bluetooth advertising permissions already granted
            callback(true)
        }
    }
}
