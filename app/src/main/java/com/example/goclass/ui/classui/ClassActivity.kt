/*
 * ClassActivity serves as the main activity for the GoClass application, hosting the navigation graph
 * for navigating between different fragments within the context of a class.
 *
 * The activity retrieves information about the selected class (classId and className) from the intent,
 * stores this information in SharedPreferences ("ClassPrefs"), and sets up the navigation using a NavController.
 *
 * @navController: NavController responsible for handling navigation within the activity.
 * @binding: View binding instance for accessing views in the activity layout.
 *
 * onCreate: Initializes the activity, retrieves class information from the intent, stores it in SharedPreferences,
 *           and sets up the navigation using NavController.
 * onSupportNavigateUp: Handles the Up button press to navigate up in the application's navigation hierarchy.
 */

package com.example.goclass.ui.classui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.goclass.R
import com.example.goclass.databinding.ActivityClassBinding

class ClassActivity : AppCompatActivity() {
    // NavController for handling navigation within the activity
    private lateinit var navController: NavController

    // View binding instance for accessing views in the activity layout
    private lateinit var binding: ActivityClassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View binding initialization
        binding = ActivityClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieving class information from the intent
        val classSharedPref = getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = intent.getIntExtra("classId", -1)!!
        val className = intent.getStringExtra("className")!!

        // Storing class information in SharedPreferences
        with(classSharedPref?.edit()) {
            this?.putInt("classId", classId)
            this?.putString("className", className)
            this?.apply()
        }

        // Setting up navigation using NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_class) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handling the Up button press to navigate up in the application's navigation hierarchy
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
