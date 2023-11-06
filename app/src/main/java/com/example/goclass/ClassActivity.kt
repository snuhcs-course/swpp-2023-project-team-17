package com.example.goclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.goclass.dataClass.MessageDummy
import com.example.goclass.databinding.ActivityClassBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassBinding

    private val messages =
        listOf(
            MessageDummy("Recycler View Example #1"),
            MessageDummy("Recycler View Example #2"),
            MessageDummy("Recycler View Example #3"),
            MessageDummy("Recycler View Example #4"),
            MessageDummy("Recycler View Example #5"),
            MessageDummy("Recycler View Example #6"),
            MessageDummy("Recycler View Example #7"),
            MessageDummy("Recycler View Example #8"),
            MessageDummy("Recycler View Example #9"),
            MessageDummy("Recycler View Example #10"),
            MessageDummy("Recycler View Example #11"),
            MessageDummy("Recycler View Example #12"),
            MessageDummy("Recycler View Example #13"),
            MessageDummy("Recycler View Example #14"),
            MessageDummy("Recycler View Example #15"),
            MessageDummy("Recycler View Example #16"),
            MessageDummy("Recycler View Example #17"),
            MessageDummy("Recycler View Example #18"),
            MessageDummy("Recycler View Example #19"),
            MessageDummy("Recycler View Example #20"),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userSharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userRole = userSharedPref.getString("userRole", "") ?: ""

        val classSharedPref = getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = intent.getIntExtra("classId", -1)!!
        val className = intent.getStringExtra("className")!!
        with(classSharedPref?.edit()) {
            this?.putInt("classId", classId)
            this?.putString("className", className)
            this?.apply()
        }

        initViews(className)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_class)
        navView.setupWithNavController(navController)

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Attendance Button
        binding.attendanceButton.setOnClickListener {
            when (userRole) {
                "student" -> {
                    val intent = Intent(this, StudentAttendanceActivity::class.java)
                    startActivity(intent)
                }
                "professor" -> {
                    val intent = Intent(this, ProfessorAttendanceActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Invalid user role", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initViews(className: String) {
//        binding.messageList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding.messageList.adapter = MessageAdapter(messages)

        binding.className.text = className
    }
}
