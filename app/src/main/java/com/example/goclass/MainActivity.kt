package com.example.goclass

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // User role from ClassActivity back button
        if(intent.hasExtra("userRole")) {
            val userRole = intent.getStringExtra("userRole")
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
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
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
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
}
