package com.example.goclass

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.adapter.StudentAttendanceAdapter
import com.example.goclass.databinding.ActivityStudentAttendanceBinding
import kotlinx.coroutines.launch
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
        val studentAttendanceAdapter = StudentAttendanceAdapter()
        binding.studentAttendanceRecyclerView.adapter = studentAttendanceAdapter
        binding.studentAttendanceRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lifecycleScope.launch {
            try {
                val studentAttendanceList = viewModel.getStudentAttendanceList(classId, userId)
                studentAttendanceAdapter.setStudentAttendanceList(studentAttendanceList)
            } catch (e: Exception) {
                Log.e("studentAttendanceListError", e.message.toString())
            }
        }
    }
}
