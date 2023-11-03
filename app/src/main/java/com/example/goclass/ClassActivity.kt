package com.example.goclass

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

        val className = intent.getStringExtra("className")!!

        initViews(className)

        // User role from Professor(Student)Fragment class button
        val userRole = intent.getStringExtra("userRole")

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_class)

        navView.setupWithNavController(navController)

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }

        // Attendance Button
        binding.attendanceButton.setOnClickListener {
            when (userRole) {
                "student" -> {
                    val intent = Intent(this, StudentAttendanceActivity::class.java)
                    intent.putExtra("userRole", userRole)
                    intent.putExtra("className", className)
                    startActivity(intent)
                }
                "professor" -> {
                    Toast.makeText(this, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, ProfessorAttendanceActivity::class.java)
//                    intent.putExtra("userRole", userRole)
//                    intent.putExtra("className", className)
//                    startActivity(intent)
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
