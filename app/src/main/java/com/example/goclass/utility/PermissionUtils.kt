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

    fun requestBluetoothPermissions(callback: (Boolean) -> Unit) {
        val bluetoothPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            Manifest.permission.BLUETOOTH
        }

        if (ContextCompat.checkSelfPermission(context, bluetoothPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(bluetoothPermission), 101)
        }

        callback(true)
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
