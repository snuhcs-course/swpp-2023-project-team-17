package com.example.goclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.ProfessorAttendanceAdapter
import com.example.goclass.dataClass.ProfessorAttendanceDummy
import com.example.goclass.databinding.ActivityProfessorAttendanceBinding

class ProfessorAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfessorAttendanceBinding

    private val professorAttendances =
        listOf(
            ProfessorAttendanceDummy("Recycler View Example #1"),
            ProfessorAttendanceDummy("Recycler View Example #2"),
            ProfessorAttendanceDummy("Recycler View Example #3"),
            ProfessorAttendanceDummy("Recycler View Example #4"),
            ProfessorAttendanceDummy("Recycler View Example #5"),
            ProfessorAttendanceDummy("Recycler View Example #6"),
            ProfessorAttendanceDummy("Recycler View Example #7"),
            ProfessorAttendanceDummy("Recycler View Example #8"),
            ProfessorAttendanceDummy("Recycler View Example #9"),
            ProfessorAttendanceDummy("Recycler View Example #10"),
            ProfessorAttendanceDummy("Recycler View Example #11"),
            ProfessorAttendanceDummy("Recycler View Example #12"),
            ProfessorAttendanceDummy("Recycler View Example #13"),
            ProfessorAttendanceDummy("Recycler View Example #14"),
            ProfessorAttendanceDummy("Recycler View Example #15"),
            ProfessorAttendanceDummy("Recycler View Example #16"),
            ProfessorAttendanceDummy("Recycler View Example #17"),
            ProfessorAttendanceDummy("Recycler View Example #18"),
            ProfessorAttendanceDummy("Recycler View Example #19"),
            ProfessorAttendanceDummy("Recycler View Example #20"),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfessorAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        val userRole = "professor"

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, ClassActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }
    }

    private fun initViews() {
        binding.attendanceList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.attendanceList.adapter = ProfessorAttendanceAdapter(professorAttendances)
    }
}
