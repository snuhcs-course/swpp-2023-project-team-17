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
import com.example.goclass.mainUi.StudentMainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentAttendanceBinding
    private lateinit var locationReceiver: BroadcastReceiver
    private val viewModel: StudentAttendanceViewModel by viewModel()

    // Error range of location distance
    private val epsilon = 0.1

    // GPS Data of Classroom
    private val classLocation = listOf(37.42, -122.08)

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
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


        /*
        // Receive location and check "In Class"
        locationReceiver =
            object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context?,
                    intent: Intent?,
                ) {
                    if (intent?.action == "LocationUpdate") {
                        val latitude = intent.getStringExtra("latitude")?.toDouble() ?: 0.0
                        val longitude = intent.getStringExtra("longitude")?.toDouble() ?: 0.0

                        val isInClass =
                            kotlin.math.abs(latitude - classLocation[0]) < epsilon &&
                                kotlin.math.abs(longitude - classLocation[1]) < epsilon

                        if (isInClass) {
                            binding.inClassButton.text = "In Class"
                            binding.inClassButton.setBackgroundResource(R.drawable.inclass_btn_bg)
                        }
                    }
                }
            }
        registerReceiver(
            locationReceiver,
            IntentFilter("LocationUpdate"),
        )
        */
    }

    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(locationReceiver)
    }
}
