package com.example.goclass.utility

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class PermissionUtils(private val context: Context) {
    private val fineLocationPermissionCode = 1000
    private val backgroundLocationPermissionCode = 1001
    private val bluetoothPermissionCode = 101

    private var locationPermissionDeniedCount = 0

    fun requestLocationPermissions() {
        // Request ACCESS_FINE_LOCATION first
        if (locationPermissionDeniedCount < 2 || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionDeniedCount++
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                fineLocationPermissionCode
            )
        } else {
            showPermissionDeniedDialog()
        }
    }

    private fun requestBackgroundLocationPermission() {
        // Request ACCESS_BACKGROUND_LOCATION if ACCESS_FINE_LOCATION is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                backgroundLocationPermissionCode
            )
        }
    }

    private fun requestBluetoothPermissions() {
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

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(context)
            .setTitle("알림 권한 필요")
            .setMessage("이 앱의 기능을 사용하려면 알림 권한이 필요합니다. 설정 화면으로 이동하시겠습니까?")
            .setPositiveButton("이동") { dialog, which ->
                openNotificationSettings()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showBackgroundLocationPermissionDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AlertDialog.Builder(context)
                .setTitle("위치 권한 항상 허용 필요")
                .setMessage("이 앱의 기능을 사용하려면 위치 권한이 \"항상 허용\"으로 설정되어야 합니다. 설정 화면으로 이동하시겠습니까?")
                .setPositiveButton("이동") { dialog, which ->
                    requestBackgroundLocationPermission()
                }
                .setNegativeButton("취소", null)
                .show()
        }
    }

    fun showPermissionDeniedDialog() {
        AlertDialog.Builder(context)
            .setTitle("<위치 항상 허용>, <근처 기기>, <알림> 권한 필요")
            .setMessage("이 앱의 기능을 사용하려면 위 권한이 모두 필요합니다. 설정 화면으로 이동하시겠습니까? \n취소를 누르시면 앱을 정상적으로 사용하실 수 없습니다.")
            .setPositiveButton("이동") { dialog, which ->
                navigateToAppPermissionSettings()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    fun checkPermissions(): Boolean {
        val fineLocationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val backgroundLocationGranted =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
            } else {
                true // On versions prior to Android Q, background location permission is not required
            }

        val bluetoothPermissionsGranted = arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE
        ).all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        val notificationPermissionGranted = NotificationManagerCompat.from(context).areNotificationsEnabled()

        return fineLocationGranted && backgroundLocationGranted && bluetoothPermissionsGranted && notificationPermissionGranted
    }

    private fun navigateToAppPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    private fun openNotificationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        }
    }

    fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                showNotificationPermissionDialog()
            }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            fineLocationPermissionCode -> {
                // Handle the result of ACCESS_FINE_LOCATION request
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showBackgroundLocationPermissionDialog()
                } else {
                    Log.d("Permissions", "fineLocationPermissionCode")
                    showPermissionDeniedDialog()
                }
            }
            backgroundLocationPermissionCode -> {
                // Handle the result of ACCESS_BACKGROUND_LOCATION request
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestBluetoothPermissions()
                } else {
                    Log.d("Permissions", "backgroundLocationPermissionCode")
                    showPermissionDeniedDialog()
                }
            }
            bluetoothPermissionCode -> {
                // Handle the result of Bluetooth permissions request
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    requestNotificationPermissions()
                } else {
                    Log.d("Permissions", "bluetoothPermissionCode")
                    showPermissionDeniedDialog()
                }
            }
        }
    }
}
