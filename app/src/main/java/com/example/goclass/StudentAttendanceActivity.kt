package com.example.goclass

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.goclass.databinding.ActivityStudentAttendanceBinding

class StudentAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentAttendanceBinding
    private lateinit var locationReceiver: BroadcastReceiver

    // Error range of location distance
    private val epsilon = 0.1

    // GPS Data of Classroom
    private val classLocation = listOf(37.42, -122.08)

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = "student"

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, ClassActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }

        // Receive location and check "In Class"
        locationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?,
                                   intent: Intent?,
            ) {
                if (intent?.action == "LocationUpdate") {
                    val latitude = intent.getStringExtra("latitude")?.toDouble() ?: 0.0
                    val longitude = intent.getStringExtra("longitude")?.toDouble() ?: 0.0

                    val isInClass =
                                kotlin.math.abs(latitude - classLocation[0]) < epsilon &&
                                kotlin.math.abs(longitude - classLocation[1]) < epsilon

                    if(isInClass) binding.inClassText.text = "In Class"
                    else binding.inClassText.text = "Not In Class"
                }
            }
        }
        registerReceiver(locationReceiver,
            IntentFilter("LocationUpdate"),
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(locationReceiver)
    }
}
