package com.example.goclass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.goclass.databinding.ActivityStudentAttendanceBinding

class StudentAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentAttendanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = intent.getStringExtra("userRole")

        // Back Button
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, ClassActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }
    }
}
