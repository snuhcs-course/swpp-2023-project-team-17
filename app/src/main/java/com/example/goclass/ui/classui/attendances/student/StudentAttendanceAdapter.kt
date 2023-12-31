/*
 * StudentAttendanceAdapter is a RecyclerView adapter responsible for displaying a list of student attendance records.
 * It utilizes a ViewHolder pattern for efficient view recycling.
 *
 * @param repository: AttendanceRepository for handling attendance-related data operations.
 * @param lifecycleOwner: LifecycleOwner to observe data changes.
 * @param listener: StudentAttendanceFragment for handling item click events.
 * @param viewModel: StudentAttendanceAdapterViewModel for handling data operations.
 * @param studentAttendanceList: List of AttendancesResponse objects representing student attendance records.
 *
 * The adapter displays information such as student name, attendance date, and attendance status.
 * It also provides a button to mark attendance as sent, and items are clickable to view more details.
 */

package com.example.goclass.ui.classui.attendances.student

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class StudentAttendanceAdapter(
    private val repository: AttendanceRepository,
    private val lifecycleOwner: LifecycleOwner,
    private val listener: StudentAttendanceFragment,
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
        return StudentAttendanceViewHolder(binding, viewModel, lifecycleOwner, listener)
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
        private val listener: StudentAttendanceFragment,
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(studentAttendanceItem: AttendancesResponse) {
            val attendanceId = studentAttendanceItem.attendanceId
            val studentName = studentAttendanceItem.studentName ?: ""

            val originalFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("UTC")
            val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date? = originalFormat.parse(studentAttendanceItem.attendanceDate)
            val dateText = date?.let { targetFormat.format(it) } ?: ""

            binding.attendanceDateText.text = dateText

            val classLength = studentAttendanceItem.attendanceDetail.length

            if (classLength > 1) {
                val attendanceDuration = studentAttendanceItem.attendanceDuration * 100
                val durationPercentage = attendanceDuration.div(classLength - 1)
                binding.durationText.text = "$durationPercentage%"
            } else {
                binding.durationText.text = "0%"
            }

            val attendanceStatus = studentAttendanceItem.attendanceStatus

            when (attendanceStatus) {
                2 -> binding.attendanceStatusText.text = "Present"
                1 -> {
                    binding.attendanceStatusText.text = "Late"
                    binding.attendanceStatusText.background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.late_bg)
                }
                else -> {
                    binding.attendanceStatusText.text = "Absent"
                    binding.attendanceStatusText.background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.absent_bg)
                }
            }

            val isSent = studentAttendanceItem.isSent
            if (isSent == 1) {
                binding.sendButton.isEnabled = false
                binding.sendButton.text = "Sent"
                val gray = ContextCompat.getColor(itemView.context, R.color.gray)
                binding.sendButton.setTextColor(gray)
                binding.sendButton.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.sent_bg)
            }

            if (binding.sendButton.isEnabled) {
                binding.sendButton.setOnClickListener {
                    viewModel.editAttendance(attendanceId)
                    viewModel.editSuccess.observe(lifecycleOwner) { editSuccess ->
                        if (editSuccess) {
                            binding.sendButton.isEnabled = false
                            binding.sendButton.text = "Sent"
                            val gray = ContextCompat.getColor(itemView.context, R.color.gray)
                            binding.sendButton.setTextColor(gray)
                            binding.sendButton.background =
                                ContextCompat.getDrawable(itemView.context, R.drawable.sent_bg)
                        }
                    }
                }
            }
            itemView.setOnClickListener {
                listener.onItemClicked(
                    attendanceId,
                    studentName,
                    dateText,
                    attendanceStatus,
                )
            }
            binding.attendanceStatusText.setOnClickListener {
                listener.onItemClicked(
                    attendanceId,
                    studentName,
                    dateText,
                    attendanceStatus,
                )
            }
        }
    }
}
