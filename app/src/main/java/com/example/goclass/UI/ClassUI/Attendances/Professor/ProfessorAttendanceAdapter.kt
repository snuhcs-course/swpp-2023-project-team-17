package com.example.goclass.UI.ClassUI.Attendances.Professor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.Network.DataClass.AttendancesResponse
import com.example.goclass.databinding.ItemProfessorAttendanceBinding

class ProfessorAttendanceAdapter(
    private val listener: ProfessorAttendanceFragment,
) :
    RecyclerView.Adapter<ProfessorAttendanceAdapter.ProfessorAttendanceViewHolder>() {
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
        return ProfessorAttendanceViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ProfessorAttendanceViewHolder,
        position: Int,
    ) {
        val studentAttendanceItem = professorAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    override fun getItemCount(): Int = professorAttendanceList.size

    class ProfessorAttendanceViewHolder(
        var binding: ItemProfessorAttendanceBinding,
        private val listener: ProfessorAttendanceFragment,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(professorAttendanceItem: AttendancesResponse) {
            binding.attendanceDateText.text = professorAttendanceItem.attendanceDate
            binding.studentListButton.setOnClickListener {
                listener.onItemClicked(professorAttendanceItem.attendanceDate)
            }
        }
    }
}
