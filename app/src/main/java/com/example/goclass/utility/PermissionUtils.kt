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
        // Android 12 (API 레벨 31) 이상의 경우 BLUETOOTH_CONNECT 권한을 체크
        val bluetoothPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            // 이전 버전의 경우 기본 BLUETOOTH 권한 사용
            Manifest.permission.BLUETOOTH
        }

        if (ContextCompat.checkSelfPermission(activity, bluetoothPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(bluetoothPermission), requestCode)
            return false
        }

        return true
    }
}
