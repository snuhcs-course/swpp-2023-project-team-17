package com.example.goclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.ProfessorAttendanceListAdapter
import com.example.goclass.databinding.ActivityProfessorAttendanceListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorAttendanceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfessorAttendanceListBinding
    private val viewModel: ProfessorAttendanceListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfessorAttendanceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userSharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userSharedPref!!.getInt("userId", -1)

        val date = intent.getStringExtra("date")!!

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, ProfessorAttendanceActivity::class.java)
            startActivity(intent)
        }

        // show professorStudentAttendanceList with dummy data
        val userMap = mapOf("userId" to "1", "userType" to "1")
        val professorAttendanceListAdapter = ProfessorAttendanceListAdapter()
        binding.professorAttendanceListRecyclerView.adapter = professorAttendanceListAdapter
        binding.professorAttendanceListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val studentAttendanceListLiveData = viewModel.getStudentAttendanceList(date, userMap)
        studentAttendanceListLiveData.observe(this) { studentAttendanceList ->
            professorAttendanceListAdapter.setStudentAttendanceList(studentAttendanceList)
        }
    }
}
