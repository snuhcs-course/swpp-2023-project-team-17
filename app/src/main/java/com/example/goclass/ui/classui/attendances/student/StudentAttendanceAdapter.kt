package com.example.goclass.ui.classui.attendances.student

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.R
import com.example.goclass.databinding.ItemStudentAttendanceBinding
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.AttendanceRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class StudentAttendanceAdapter(
    private val repository: AttendanceRepository,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceViewHolder>() {
    private val viewModel = StudentAttendanceAdapterViewModel(repository)
    private var studentAttendanceList = listOf<AttendancesResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setStudentAttendanceList(list: List<AttendancesResponse>) {
        studentAttendanceList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StudentAttendanceViewHolder {
        val binding =
            ItemStudentAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return StudentAttendanceViewHolder(binding, viewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(
        holder: StudentAttendanceViewHolder,
        position: Int,
    ) {
        val studentAttendanceItem = studentAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    override fun getItemCount(): Int = studentAttendanceList.size

    @OptIn(DelicateCoroutinesApi::class)
    class StudentAttendanceViewHolder(
        val binding: ItemStudentAttendanceBinding,
        val viewModel: StudentAttendanceAdapterViewModel,
        val lifecycleOwner: LifecycleOwner,
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(studentAttendanceItem: AttendancesResponse) {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("UTC")
            val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date? = originalFormat.parse(studentAttendanceItem.attendanceDate)
            binding.attendanceDateText.text = date?.let { targetFormat.format(it) }

            val attendanceStatus = studentAttendanceItem.attendanceStatus
            if (attendanceStatus == 2) {
                binding.attendanceStatusText.text = "Present"
            } else if (attendanceStatus == 1) {
                binding.attendanceStatusText.text = "Late"
                binding.attendanceStatusText.background = ContextCompat.getDrawable(itemView.context, R.drawable.late_bg)
            } else {
                binding.attendanceStatusText.text = "Absent"
                binding.attendanceStatusText.background = ContextCompat.getDrawable(itemView.context, R.drawable.absent_bg)
            }

            val isSent = studentAttendanceItem.isSent
            if (isSent == 1) {
                binding.sendButton.isEnabled = false
                binding.sendButton.text = "Sent"
                val gray = ContextCompat.getColor(itemView.context, R.color.gray)
                binding.sendButton.setTextColor(gray)
                binding.sendButton.background = ContextCompat.getDrawable(itemView.context, R.drawable.sent_bg)
            }

            if (binding.sendButton.isEnabled) {
                binding.sendButton.setOnClickListener {
                    val attendanceId = studentAttendanceItem.attendanceId
                    viewModel.editAttendance(attendanceId)
                    viewModel.editSuccess.observe(lifecycleOwner) { editSuccess ->
                        if (editSuccess) {
                            binding.sendButton.isEnabled = false
                            binding.sendButton.text = "Sent"
                            val gray = ContextCompat.getColor(itemView.context, R.color.gray)
                            binding.sendButton.setTextColor(gray)
                            binding.sendButton.background = ContextCompat.getDrawable(itemView.context, R.drawable.sent_bg)
                        }
                    }
                }
            }
        }
    }
}
