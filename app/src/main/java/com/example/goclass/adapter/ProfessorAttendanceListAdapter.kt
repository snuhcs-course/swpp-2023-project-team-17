package com.example.goclass.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.dataClass.ProfessorAttendanceListDummy
import com.example.goclass.databinding.ItemProfessorAttendanceListBinding

class ProfessorAttendanceListAdapter(private val professorAttendanceLists: List<ProfessorAttendanceListDummy>) :
    RecyclerView.Adapter<ProfessorAttendanceListAdapter.ProfessorAttendanceListViewHolder>() {
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
        holder.bind(professorAttendanceLists[position])
    }

    override fun getItemCount(): Int = professorAttendanceLists.size

    class ProfessorAttendanceListViewHolder(var binding: ItemProfessorAttendanceListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(professorAttendanceList: ProfessorAttendanceListDummy) {
            binding.attendanceText.text = professorAttendanceList.content
        }
    }
}
