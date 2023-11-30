package com.example.goclass.ui.mainui

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.goclass.R
import com.example.goclass.databinding.ActivityMainBinding
import com.example.goclass.ui.classui.attendances.service.LocationService
import com.example.goclass.utility.PermissionUtils

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionUtils = PermissionUtils(this)

        permissionUtils.requestBluetoothAdvertisePermissionsWithCallback { bluetoothAdvertPermissionsGranted ->
            if (bluetoothAdvertPermissionsGranted) {
                permissionUtils.requestBluetoothPermissions { bluetoothScanPermissionsGranted ->
                    if (bluetoothScanPermissionsGranted) {
                        permissionUtils.requestLocationPermissions()
                    }
                }
            }
        }
//        permissionUtils.requestBluetoothAdvertisePermissionsWithCallback()

//        permissionUtils.requestBluetoothPermissions()
//        permissionUtils.requestLocationPermissions()

        //startLocationService()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Check if logged in
        checkLoginStatus()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkLoginStatus() {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val userRole = sharedPref.getString("userRole", "") ?: ""

        if (isLoggedIn) {
            when (userRole) {
                "student" -> {
                    navController.navigate(R.id.studentMainFragment)
                }
                "professor" -> {
                    navController.navigate(R.id.professorMainFragment)
                }
                else -> {
                    navController.navigate(R.id.profileFragment)
                }
            }
        } else {
            navController.navigate(R.id.loginFragment)
        }
    }

    private fun startLocationService() {
        Log.d("Debug", "before starting LocationService")
        startService(Intent(this, LocationService::class.java))
        Log.d("Debug", "after starting LocationService")
    }

    companion object {
        private const val BLUETOOTH_REQUEST_CODE = 101 // 블루투스 권한 요청 코드
    }
}
