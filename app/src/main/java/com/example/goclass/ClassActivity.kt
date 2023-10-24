package com.example.goclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.MessageAdapter
import com.example.goclass.dataClass.Message
import com.example.goclass.databinding.ActivityClassBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassBinding

    private val messages =
        listOf(
            Message("Recycler View Example #1"),
            Message("Recycler View Example #2"),
            Message("Recycler View Example #3"),
            Message("Recycler View Example #4"),
            Message("Recycler View Example #5"),
            Message("Recycler View Example #6"),
            Message("Recycler View Example #7"),
            Message("Recycler View Example #8"),
            Message("Recycler View Example #9"),
            Message("Recycler View Example #10"),
            Message("Recycler View Example #11"),
            Message("Recycler View Example #12"),
            Message("Recycler View Example #13"),
            Message("Recycler View Example #14"),
            Message("Recycler View Example #15"),
            Message("Recycler View Example #16"),
            Message("Recycler View Example #17"),
            Message("Recycler View Example #18"),
            Message("Recycler View Example #19"),
            Message("Recycler View Example #20"),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

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

    private fun initViews() {
        binding.messageList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.messageList.adapter = MessageAdapter(messages)
    }
}
