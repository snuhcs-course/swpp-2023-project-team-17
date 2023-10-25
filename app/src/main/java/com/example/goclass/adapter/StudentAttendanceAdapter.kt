package com.example.goclass.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.dataClass.StudentAttendance
import com.example.goclass.databinding.ItemStudentAttendanceBinding

class StudentAttendanceAdapter(private val studentAttendances: List<StudentAttendance>) :
    RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StudentAttendanceViewHolder {
        var binding =
            ItemStudentAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return StudentAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StudentAttendanceViewHolder,
        position: Int,
    ) {
        holder.bind(studentAttendances[position])
    }

    override fun getItemCount(): Int = studentAttendances.size

    class StudentAttendanceViewHolder(var binding: ItemStudentAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(studentAttendance: StudentAttendance) {
            binding.attendanceText.text = studentAttendance.content
        }
    }
}
