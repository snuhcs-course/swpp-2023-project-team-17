package com.example.goclass.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.dataClass.AttendancesResponse
import com.example.goclass.databinding.ItemStudentAttendanceBinding

class StudentAttendanceAdapter : RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceViewHolder>() {
    private var studentAttendanceList = listOf<AttendancesResponse>()

    fun setStudentAttendanceList(list: List<AttendancesResponse>) {
        studentAttendanceList = list
        notifyDataSetChanged()
    }

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
        val studentAttendanceItem = studentAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    override fun getItemCount(): Int = studentAttendanceList.size

    class StudentAttendanceViewHolder(var binding: ItemStudentAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(studentAttendanceItem: AttendancesResponse) {
            binding.attendanceDateText.text = studentAttendanceItem.attendanceDate
            val attendanceStatus = studentAttendanceItem.attendanceStatus
            if(attendanceStatus == 1) binding.attendanceStatusText.text = "Present"
            else binding.attendanceStatusText.text = "Absent"
        }
    }
}
