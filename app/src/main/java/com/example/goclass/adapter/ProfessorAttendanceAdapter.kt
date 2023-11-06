package com.example.goclass.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.ClassActivity
import com.example.goclass.ProfessorAttendanceListActivity
import com.example.goclass.dataClass.AttendancesResponse
import com.example.goclass.databinding.ItemProfessorAttendanceBinding

class ProfessorAttendanceAdapter : RecyclerView.Adapter<ProfessorAttendanceAdapter.ProfessorAttendanceViewHolder>() {
    private var professorAttendanceList = listOf<AttendancesResponse>()

    fun setProfessorAttendanceList(list: List<AttendancesResponse>) {
        professorAttendanceList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProfessorAttendanceViewHolder {
        var binding =
            ItemProfessorAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ProfessorAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProfessorAttendanceViewHolder,
        position: Int,
    ) {
        val studentAttendanceItem = professorAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    override fun getItemCount(): Int = professorAttendanceList.size

    class ProfessorAttendanceViewHolder(var binding: ItemProfessorAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(professorAttendanceItem: AttendancesResponse) {
            binding.attendanceDateText.text = professorAttendanceItem.attendanceDate
            binding.studentListButton.setOnClickListener {
                val intent = Intent(itemView.context, ProfessorAttendanceListActivity::class.java)
                intent.putExtra("date", professorAttendanceItem.attendanceDate)
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}
