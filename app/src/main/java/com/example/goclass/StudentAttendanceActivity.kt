package com.example.goclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.StudentAttendanceAdapter
import com.example.goclass.dataClass.StudentAttendance
import com.example.goclass.databinding.ActivityStudentAttendanceBinding

class StudentAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentAttendanceBinding

    private val studentAttendances =
        listOf(
        StudentAttendance("Recycler View Example #1"),
        StudentAttendance("Recycler View Example #2"),
        StudentAttendance("Recycler View Example #3"),
        StudentAttendance("Recycler View Example #4"),
        StudentAttendance("Recycler View Example #5"),
        StudentAttendance("Recycler View Example #6"),
        StudentAttendance("Recycler View Example #7"),
        StudentAttendance("Recycler View Example #8"),
        StudentAttendance("Recycler View Example #9"),
        StudentAttendance("Recycler View Example #10"),
        StudentAttendance("Recycler View Example #11"),
        StudentAttendance("Recycler View Example #12"),
        StudentAttendance("Recycler View Example #13"),
        StudentAttendance("Recycler View Example #14"),
        StudentAttendance("Recycler View Example #15"),
        StudentAttendance("Recycler View Example #16"),
        StudentAttendance("Recycler View Example #17"),
        StudentAttendance("Recycler View Example #18"),
        StudentAttendance("Recycler View Example #19"),
        StudentAttendance("Recycler View Example #20"),
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        val userRole = "student"

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, ClassActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }
    }

    private fun initViews() {
        binding.attendanceList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.attendanceList.adapter = StudentAttendanceAdapter(studentAttendances)
    }
}
