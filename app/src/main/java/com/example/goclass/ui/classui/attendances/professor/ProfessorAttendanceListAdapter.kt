package com.example.goclass.ui.classui.attendances.professor

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.R
import com.example.goclass.databinding.ItemProfessorAttendanceListBinding
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfessorAttendanceListAdapter(
    private val listener: ProfessorAttendanceListFragment,
) : RecyclerView.Adapter<ProfessorAttendanceListAdapter.ProfessorAttendanceListViewHolder>() {
    private var studentAttendanceList = listOf<AttendancesResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setStudentAttendanceList(list: List<AttendancesResponse>) {
        studentAttendanceList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProfessorAttendanceListViewHolder {
        val binding =
            ItemProfessorAttendanceListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ProfessorAttendanceListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: ProfessorAttendanceListViewHolder,
        position: Int,
    ) {
        val studentAttendanceItem = studentAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    override fun getItemCount(): Int = studentAttendanceList.size

    class ProfessorAttendanceListViewHolder(
        val binding: ItemProfessorAttendanceListBinding,
        private val listener: ProfessorAttendanceListFragment,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(studentAttendanceItem: AttendancesResponse) {
            val attendanceId = studentAttendanceItem.attendanceId
            val studentName = studentAttendanceItem.studentName?:""
            val attendanceStatus = studentAttendanceItem.attendanceStatus

            val classLength = studentAttendanceItem.attendanceDetail.length
            val attendanceDuration = studentAttendanceItem.attendanceDuration * 100
            val durationPercentage = attendanceDuration.div(classLength - 1)

            binding.durationText.text = "Attendance Percentage: $durationPercentage%"
            binding.studentNameText.text = studentName

            when (attendanceStatus) {
                2 -> {
                    binding.attendanceStatusText.text = "Present"
                }
                1 -> {
                    binding.attendanceStatusText.text = "Late"
                    binding.attendanceStatusText.background = ContextCompat.getDrawable(itemView.context, R.drawable.late_bg)
                }
                else -> {
                    binding.attendanceStatusText.text = "Absent"
                    binding.attendanceStatusText.background = ContextCompat.getDrawable(itemView.context, R.drawable.absent_bg)
                }
            }
            itemView.setOnClickListener {
                listener.onItemClicked(
                    attendanceId,
                    studentName,
                    attendanceStatus,
                )
            }
            binding.attendanceStatusText.setOnClickListener {
                listener.onItemClicked(
                    attendanceId,
                    studentName,
                    attendanceStatus,
                )
            }
        }
    }
}
