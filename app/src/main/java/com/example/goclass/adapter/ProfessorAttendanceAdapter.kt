package com.example.goclass.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.ProfessorAttendanceActivity
import com.example.goclass.ProfessorAttendanceListActivity
import com.example.goclass.dataClass.ProfessorAttendance
import com.example.goclass.databinding.ItemProfessorAttendanceBinding

class ProfessorAttendanceAdapter(private val professorAttendances: List<ProfessorAttendance>)
    : RecyclerView.Adapter<ProfessorAttendanceAdapter.ProfessorAttendanceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfessorAttendanceViewHolder {
        var binding = ItemProfessorAttendanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfessorAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfessorAttendanceViewHolder, position: Int) {
        holder.bind(professorAttendances[position])
        holder.binding.studentListButton.setOnClickListener {
            val intent = Intent(holder.binding.root.context, ProfessorAttendanceListActivity::class.java)
            holder.binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = professorAttendances.size

    class ProfessorAttendanceViewHolder(var binding: ItemProfessorAttendanceBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(professorAttendance: ProfessorAttendance) {
                binding.attendanceText.text = professorAttendance.content
            }
        }
}