package com.example.goclass.UI.ClassUI

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.goclass.R
import com.example.goclass.databinding.ActivityClassBinding

class ClassActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityClassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val classSharedPref = getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = intent.getIntExtra("classId", -1)!!
        val className = intent.getStringExtra("className")!!
        with(classSharedPref?.edit()) {
            this?.putInt("classId", classId)
            this?.putString("className", className)
            this?.apply()
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_class) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
