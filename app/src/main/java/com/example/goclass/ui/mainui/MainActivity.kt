package com.example.goclass.ui.mainui

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.goclass.R
import com.example.goclass.databinding.ActivityMainBinding
import com.example.goclass.utility.PermissionUtils

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionUtils: PermissionUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionUtils = PermissionUtils(this)
        permissionUtils.requestLocationPermissions()
//        permissionUtils.requestBluetoothPermissions()

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
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkLoginStatus() {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val userRole = sharedPref.getString("userRole", "") ?: ""

        if (isLoggedIn) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build()

            when (userRole) {
                "student" -> {
                    navController.navigate(R.id.studentMainFragment, null, navOptions)
                }
                "professor" -> {
                    navController.navigate(R.id.professorMainFragment, null, navOptions)
                }
                else -> {
                    navController.navigate(R.id.profileFragment, null, navOptions)
                }
            }
        } else {
            navController.navigate(R.id.loginFragment)
        }
    }

    companion object {
        private const val BLUETOOTH_REQUEST_CODE = 101 // 블루투스 권한 요청 코드
    }
}
