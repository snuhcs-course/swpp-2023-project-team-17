package com.example.goclass.ui.classui.attendances.professor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.databinding.ItemProfessorAttendanceBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("UTC")
            val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date = originalFormat.parse(professorAttendanceItem.attendanceDate)

            binding.attendanceDateText.text = targetFormat.format(date)
            binding.studentListButton.setOnClickListener {
                listener.onItemClicked(professorAttendanceItem.attendanceDate)
            }
        }
    }
}
