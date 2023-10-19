package com.example.goclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.goclass.databinding.ActivityProfessorAttendanceListBinding

class ProfessorAttendanceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfessorAttendanceListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfessorAttendanceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = intent.getStringExtra("userRole")

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(this, ProfessorAttendanceActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }
    }
}
