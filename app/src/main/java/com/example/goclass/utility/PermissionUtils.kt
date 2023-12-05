package com.example.goclass.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class PermissionUtils(private val context: Context) {
    fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
        }
    }

    fun requestBluetoothPermissions() {
        val permissionsToRequest = mutableListOf<String>()

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
            ActivityCompat.requestPermissions(context as Activity, permissionsToRequest.toTypedArray(), 101)
        }
    }

    fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                context.startActivity(intent)
            }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                requestNotificationPermissions()
            }
        }
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
