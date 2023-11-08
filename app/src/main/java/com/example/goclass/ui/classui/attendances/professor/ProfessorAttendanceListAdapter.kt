package com.example.goclass.ui.classui.attendances.professor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.databinding.ItemProfessorAttendanceListBinding

class ProfessorAttendanceListAdapter : RecyclerView.Adapter<ProfessorAttendanceListAdapter.ProfessorAttendanceListViewHolder>() {
    private var studentAttendanceList = listOf<AttendancesResponse>()

    fun setStudentAttendanceList(list: List<AttendancesResponse>) {
        studentAttendanceList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProfessorAttendanceListViewHolder {
        var binding =
            ItemProfessorAttendanceListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ProfessorAttendanceListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProfessorAttendanceListViewHolder,
        position: Int,
    ) {
        val studentAttendanceItem = studentAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    override fun getItemCount(): Int = studentAttendanceList.size

    class ProfessorAttendanceListViewHolder(var binding: ItemProfessorAttendanceListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(studentAttendanceItem: AttendancesResponse) {
            binding.studentIdText.text = studentAttendanceItem.studentId.toString()
            when (studentAttendanceItem.attendanceStatus) {
                2 -> {
                    binding.attendanceStatusText.text = "Present"
                }
                1 -> {
                    binding.attendanceStatusText.text = "Late"
                }
                else -> {
                    binding.attendanceStatusText.text = "Absent"
                }
            }
        }
    }
}
