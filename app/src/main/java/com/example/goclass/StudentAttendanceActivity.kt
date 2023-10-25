package com.example.goclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.StudentAttendanceAdapter
import com.example.goclass.dataClass.StudentAttendanceDummy
import com.example.goclass.databinding.ActivityStudentAttendanceBinding

class StudentAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentAttendanceBinding

    private val studentAttendances =
        listOf(
            StudentAttendanceDummy("Recycler View Example #1"),
            StudentAttendanceDummy("Recycler View Example #2"),
            StudentAttendanceDummy("Recycler View Example #3"),
            StudentAttendanceDummy("Recycler View Example #4"),
            StudentAttendanceDummy("Recycler View Example #5"),
            StudentAttendanceDummy("Recycler View Example #6"),
            StudentAttendanceDummy("Recycler View Example #7"),
            StudentAttendanceDummy("Recycler View Example #8"),
            StudentAttendanceDummy("Recycler View Example #9"),
            StudentAttendanceDummy("Recycler View Example #10"),
            StudentAttendanceDummy("Recycler View Example #11"),
            StudentAttendanceDummy("Recycler View Example #12"),
            StudentAttendanceDummy("Recycler View Example #13"),
            StudentAttendanceDummy("Recycler View Example #14"),
            StudentAttendanceDummy("Recycler View Example #15"),
            StudentAttendanceDummy("Recycler View Example #16"),
            StudentAttendanceDummy("Recycler View Example #17"),
            StudentAttendanceDummy("Recycler View Example #18"),
            StudentAttendanceDummy("Recycler View Example #19"),
            StudentAttendanceDummy("Recycler View Example #20"),
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
