package com.example.goclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.ProfessorAttendanceListAdapter
import com.example.goclass.dataClass.ProfessorAttendanceListDummy
import com.example.goclass.databinding.ActivityProfessorAttendanceListBinding

class ProfessorAttendanceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfessorAttendanceListBinding

    private val professorAttendanceLists =
        listOf(
            ProfessorAttendanceListDummy("Recycler View Example #1"),
            ProfessorAttendanceListDummy("Recycler View Example #2"),
            ProfessorAttendanceListDummy("Recycler View Example #3"),
            ProfessorAttendanceListDummy("Recycler View Example #4"),
            ProfessorAttendanceListDummy("Recycler View Example #5"),
            ProfessorAttendanceListDummy("Recycler View Example #6"),
            ProfessorAttendanceListDummy("Recycler View Example #7"),
            ProfessorAttendanceListDummy("Recycler View Example #8"),
            ProfessorAttendanceListDummy("Recycler View Example #9"),
            ProfessorAttendanceListDummy("Recycler View Example #10"),
            ProfessorAttendanceListDummy("Recycler View Example #11"),
            ProfessorAttendanceListDummy("Recycler View Example #12"),
            ProfessorAttendanceListDummy("Recycler View Example #13"),
            ProfessorAttendanceListDummy("Recycler View Example #14"),
            ProfessorAttendanceListDummy("Recycler View Example #15"),
            ProfessorAttendanceListDummy("Recycler View Example #16"),
            ProfessorAttendanceListDummy("Recycler View Example #17"),
            ProfessorAttendanceListDummy("Recycler View Example #18"),
            ProfessorAttendanceListDummy("Recycler View Example #19"),
            ProfessorAttendanceListDummy("Recycler View Example #20"),
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
