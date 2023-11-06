package com.example.goclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.StudentAttendanceAdapter
import com.example.goclass.databinding.ActivityStudentAttendanceBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentAttendanceBinding
    private val viewModel: StudentAttendanceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userSharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userSharedPref!!.getInt("userId", -1)

        val classSharedPref = getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val className = classSharedPref!!.getString("className", "")
        val classId = classSharedPref!!.getInt("classId", -1)

        binding.classNameText.text = className

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, ClassActivity::class.java)
            intent.putExtra("className", className)
            startActivity(intent)
        }

        // show studentAttendanceList with dummy data
        val repository: Repository by inject()
        val studentAttendanceAdapter = StudentAttendanceAdapter(repository)
        binding.studentAttendanceRecyclerView.adapter = studentAttendanceAdapter
        binding.studentAttendanceRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val studentAttendanceListLiveData = viewModel.getStudentAttendanceList(classId, userId)
        studentAttendanceListLiveData.observe(this) { studentAttendanceList ->
            studentAttendanceAdapter.setStudentAttendanceList(studentAttendanceList)
        }
    }
}
