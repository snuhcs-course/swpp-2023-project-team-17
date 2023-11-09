package com.example.goclass.ui.classui.attendances.professor

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.R
import com.example.goclass.databinding.ItemProfessorAttendanceListBinding
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.UserRepository
import java.lang.Exception
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfessorAttendanceListAdapter(
    private val repository: UserRepository,
) : RecyclerView.Adapter<ProfessorAttendanceListAdapter.ProfessorAttendanceListViewHolder>() {
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
        return ProfessorAttendanceListViewHolder(binding, repository)
    }

    override fun onBindViewHolder(
        holder: ProfessorAttendanceListViewHolder,
        position: Int,
    ) {
        val studentAttendanceItem = studentAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    override fun getItemCount(): Int = studentAttendanceList.size

    class ProfessorAttendanceListViewHolder(var binding: ItemProfessorAttendanceListBinding, val repository: UserRepository) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(studentAttendanceItem: AttendancesResponse) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = repository.userGet(studentAttendanceItem.studentId)
                    if (response.code == 200) {
                        withContext(Dispatchers.Main) {
                            binding.studentIdText.text = response.userName
                        }
                    }
                } catch (e: Exception) {
                    Log.d("userNameGetError", e.message.toString())
                }
            }
            when (studentAttendanceItem.attendanceStatus) {
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
        }
    }
}
