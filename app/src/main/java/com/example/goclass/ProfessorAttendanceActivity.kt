package com.example.goclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.ProfessorAttendanceAdapter
import com.example.goclass.databinding.ActivityProfessorAttendanceBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfessorAttendanceBinding
    private val viewModel: ProfessorAttendanceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfessorAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userSharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userSharedPref!!.getInt("userId", -1)

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, ClassActivity::class.java)
            startActivity(intent)
        }

        // show professorAttendanceList with dummy data
        val userMap = mapOf("userId" to "1", "userType" to "1")
        val professorAttendanceAdapter = ProfessorAttendanceAdapter()
        binding.professorAttendanceRecyclerView.adapter = professorAttendanceAdapter
        binding.professorAttendanceRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val professorAttendanceListLiveData = viewModel.getProfessorAttendanceList(userMap)
        professorAttendanceListLiveData.observe(this) { professorAttendanceList ->
            professorAttendanceAdapter.setProfessorAttendanceList(professorAttendanceList)
        }
    }
}
