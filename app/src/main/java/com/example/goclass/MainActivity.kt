package com.example.goclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.goclass.databinding.ActivityMainBinding
import com.example.goclass.utility.PermissionUtils

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionUtils = PermissionUtils(this)

        // start tracking location (gps) if permission granted
        while (!permissionUtils.requestLocationPermissions()) Thread.sleep(100)
        startLocationService()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // User role from ClassActivity back button
        if (intent.hasExtra("userRole")) {
            val userRole = intent.getStringExtra("userRole")
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            with(sharedPref?.edit()) {
                this?.putString("userRole", userRole)
                this?.apply()
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Check if logged in
        checkLoginStatus()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkLoginStatus() {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val userRole = sharedPref.getString("userRole", "")

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
}
