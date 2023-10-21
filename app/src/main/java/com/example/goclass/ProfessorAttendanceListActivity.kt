package com.example.goclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.ProfessorAttendanceAdapter
import com.example.goclass.adapter.ProfessorAttendanceListAdapter
import com.example.goclass.dataClass.ProfessorAttendance
import com.example.goclass.dataClass.ProfessorAttendanceList
import com.example.goclass.databinding.ActivityProfessorAttendanceListBinding

class ProfessorAttendanceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfessorAttendanceListBinding

    private val professorAttendanceLists = listOf(
        ProfessorAttendanceList("Recycler View Example #1"),
        ProfessorAttendanceList("Recycler View Example #2"),
        ProfessorAttendanceList("Recycler View Example #3"),
        ProfessorAttendanceList("Recycler View Example #4"),
        ProfessorAttendanceList("Recycler View Example #5"),
        ProfessorAttendanceList("Recycler View Example #6"),
        ProfessorAttendanceList("Recycler View Example #7"),
        ProfessorAttendanceList("Recycler View Example #8"),
        ProfessorAttendanceList("Recycler View Example #9"),
        ProfessorAttendanceList("Recycler View Example #10"),
        ProfessorAttendanceList("Recycler View Example #11"),
        ProfessorAttendanceList("Recycler View Example #12"),
        ProfessorAttendanceList("Recycler View Example #13"),
        ProfessorAttendanceList("Recycler View Example #14"),
        ProfessorAttendanceList("Recycler View Example #15"),
        ProfessorAttendanceList("Recycler View Example #16"),
        ProfessorAttendanceList("Recycler View Example #17"),
        ProfessorAttendanceList("Recycler View Example #18"),
        ProfessorAttendanceList("Recycler View Example #19"),
        ProfessorAttendanceList("Recycler View Example #20")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfessorAttendanceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, ProfessorAttendanceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        binding.attendanceList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.attendanceList.adapter = ProfessorAttendanceListAdapter(professorAttendanceLists)
    }
}
