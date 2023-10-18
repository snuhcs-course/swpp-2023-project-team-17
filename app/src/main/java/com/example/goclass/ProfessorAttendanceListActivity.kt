package com.example.goclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.goclass.databinding.ActivityProfessorAttendanceBinding
import com.example.goclass.databinding.ActivityProfessorAttendanceListBinding

class ProfessorAttendanceListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor_attendance_list)

        val userRole = intent.getStringExtra("userRole")

        // Back Button
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, ProfessorAttendanceActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }
    }
}