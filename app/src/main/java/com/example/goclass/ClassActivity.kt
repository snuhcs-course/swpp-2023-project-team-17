package com.example.goclass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.goclass.databinding.ActivityClassBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // User role from Professor(Student)Fragment class button
        val userRole = intent.getStringExtra("userRole")

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_class)

        navView.setupWithNavController(navController)

        // Back Button
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }

        // Attendance Button
        val attendanceButton = findViewById<Button>(R.id.attendanceButton)
        attendanceButton.setOnClickListener {
            when (userRole) {
                "student" -> {
                    val intent = Intent(this, StudentAttendanceActivity::class.java)
                    intent.putExtra("userRole", userRole)
                    startActivity(intent)
                }
                "professor" -> {
                    val intent = Intent(this, ProfessorAttendanceActivity::class.java)
                    intent.putExtra("userRole", userRole)
                    startActivity(intent)
                }
            }
        }
    }
}
