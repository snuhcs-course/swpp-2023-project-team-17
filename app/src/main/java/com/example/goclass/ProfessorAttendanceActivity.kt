package com.example.goclass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.ProfessorAttendanceAdapter
import com.example.goclass.dataClass.ProfessorAttendance
import com.example.goclass.databinding.ActivityProfessorAttendanceBinding

class ProfessorAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfessorAttendanceBinding

    private val professorAttendances = listOf(
        ProfessorAttendance("Recycler View Example #1"),
        ProfessorAttendance("Recycler View Example #2"),
        ProfessorAttendance("Recycler View Example #3"),
        ProfessorAttendance("Recycler View Example #4"),
        ProfessorAttendance("Recycler View Example #5"),
        ProfessorAttendance("Recycler View Example #6"),
        ProfessorAttendance("Recycler View Example #7"),
        ProfessorAttendance("Recycler View Example #8"),
        ProfessorAttendance("Recycler View Example #9"),
        ProfessorAttendance("Recycler View Example #10"),
        ProfessorAttendance("Recycler View Example #11"),
        ProfessorAttendance("Recycler View Example #12"),
        ProfessorAttendance("Recycler View Example #13"),
        ProfessorAttendance("Recycler View Example #14"),
        ProfessorAttendance("Recycler View Example #15"),
        ProfessorAttendance("Recycler View Example #16"),
        ProfessorAttendance("Recycler View Example #17"),
        ProfessorAttendance("Recycler View Example #18"),
        ProfessorAttendance("Recycler View Example #19"),
        ProfessorAttendance("Recycler View Example #20")
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
