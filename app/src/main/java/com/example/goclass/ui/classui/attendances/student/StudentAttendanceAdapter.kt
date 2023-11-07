package com.example.goclass.ui.classui.attendances.student

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.databinding.ItemStudentAttendanceBinding
import com.example.goclass.repository.AttendanceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class StudentAttendanceAdapter(
    private val repository: AttendanceRepository,
) : RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceViewHolder>() {
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
        return StudentAttendanceViewHolder(binding, repository)
    }

    override fun onBindViewHolder(
        holder: StudentAttendanceViewHolder,
        position: Int,
    ) {
        val studentAttendanceItem = studentAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    override fun getItemCount(): Int = studentAttendanceList.size

    class StudentAttendanceViewHolder(var binding: ItemStudentAttendanceBinding, val repository: AttendanceRepository) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(studentAttendanceItem: AttendancesResponse) {
            binding.attendanceDateText.text = studentAttendanceItem.attendanceDate

            val attendanceStatus = studentAttendanceItem.attendanceStatus
            if (attendanceStatus == 2) {
                binding.attendanceStatusText.text = "Present"
            } else if (attendanceStatus == 1) {
                binding.attendanceStatusText.text = "Late"
            } else {
                binding.attendanceStatusText.text = "Absent"
            }

            val isSent = studentAttendanceItem.isSent
            if (isSent == 1) {
                binding.sendButton.isEnabled = false
            }

            binding.sendButton.setOnClickListener {
                if (binding.sendButton.isEnabled) {
                    GlobalScope.launch(Dispatchers.IO) {
                        val attendanceId = studentAttendanceItem.attendanceId
                        try {
                            val response = repository.attendanceEdit(attendanceId)
                            if (response.code == 200) {
                                binding.sendButton.isEnabled = false
                            }
                        } catch (e: Exception) {
                            Log.d("attendanceSendError", e.message.toString())
                        }
                    }
                }
            }
        }
    }
}
