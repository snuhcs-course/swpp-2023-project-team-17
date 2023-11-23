package com.example.goclass.ui.mainui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    var currentFragment: String = "LoginFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionUtils = PermissionUtils(this)

        // start tracking location (gps) if permission granted
        while (!permissionUtils.requestLocationPermissions()) Thread.sleep(100)
        startLocationService()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Check if logged in
        checkLoginStatus(sharedPref)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun checkLoginStatus(sharedPref: SharedPreferences) {
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val userRole = sharedPref.getString("userRole", "") ?: ""

        if (isLoggedIn) {
            when (userRole) {
                "student" -> {
                    navController.navigate(R.id.studentMainFragment)
                    currentFragment = "StudentMainFragment"
                }
                "professor" -> {
                    navController.navigate(R.id.professorMainFragment)
                    currentFragment = "ProfessorMainFragment"
                }
                else -> {
                    navController.navigate(R.id.profileFragment)
                    currentFragment = "ProfileFragment"
                }
            }
        } else {
            navController.navigate(R.id.loginFragment)
            currentFragment = "LoginFragment"
        }
    }

    private fun startLocationService() {
        Log.d("Debug", "before starting LocationService")
        startService(Intent(this, LocationService::class.java))
        Log.d("Debug", "after starting LocationService")
    }
}
