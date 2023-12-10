/*
 * ProfessorAttendanceAdapter is a RecyclerView adapter responsible for managing and displaying
 * attendance data for a professor in the ProfessorAttendanceFragment.
 *
 * It inflates item views for professor attendance, binds data to the views, and handles user interactions.
 *
 * @param listener: ProfessorAttendanceFragment instance for handling item click events.
 * @property professorAttendanceList: List of AttendancesResponse items representing professor attendance data.
 *
 * setProfessorAttendanceList: Updates the professorAttendanceList and notifies the adapter of the data change.
 * onCreateViewHolder: Inflates the layout for professor attendance item views.
 * onBindViewHolder: Binds data to the item views based on the position in the list.
 * getItemCount: Returns the total number of professor attendance items in the list.
 * ProfessorAttendanceViewHolder: ViewHolder class responsible for holding and binding item views.
 *
 * bind: Binds data to the views within the item view.
 */

package com.example.goclass.ui.classui.attendances.professor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.databinding.ItemProfessorAttendanceBinding
import com.example.goclass.network.dataclass.AttendancesResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ProfessorAttendanceAdapter(
    private val listener: ProfessorAttendanceFragment,
) : RecyclerView.Adapter<ProfessorAttendanceAdapter.ProfessorAttendanceViewHolder>() {
    private var professorAttendanceList = listOf<AttendancesResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setProfessorAttendanceList(list: List<AttendancesResponse>) {
        professorAttendanceList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProfessorAttendanceViewHolder {
        val binding =
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
        val binding: ItemProfessorAttendanceBinding,
        private val listener: ProfessorAttendanceFragment,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(professorAttendanceItem: AttendancesResponse) {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("UTC")
            val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date? = originalFormat.parse(professorAttendanceItem.attendanceDate)
            val dateText = date?.let { targetFormat.format(it) } ?: ""

            binding.attendanceDateText.text = dateText
            binding.studentListButton.setOnClickListener {
                listener.onItemClicked(dateText)
            }
        }
    }
}
