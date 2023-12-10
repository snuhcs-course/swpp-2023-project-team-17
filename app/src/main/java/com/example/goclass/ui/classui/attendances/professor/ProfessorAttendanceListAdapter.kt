/*
 * ProfessorAttendanceListAdapter is a RecyclerView Adapter responsible for handling the professor
 * attendance list items in the UI. It binds the data to the corresponding views and provides
 * interaction callbacks to the associated fragment.
 *
 * @property listener: ProfessorAttendanceListFragment, the listener for item click events.
 *
 * setStudentAttendanceList: Updates the list of student attendance items and notifies the adapter to refresh.
 * onCreateViewHolder: Creates ViewHolder instances for professor attendance list items.
 * onBindViewHolder: Binds the data to the ViewHolder views based on the position.
 * getItemCount: Returns the total number of professor attendance items.
 * ProfessorAttendanceListViewHolder: ViewHolder class for professor attendance list items.
 *   bind: Binds the data to the views and sets up click listeners.
 */

package com.example.goclass.ui.classui.attendances.professor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.R
import com.example.goclass.databinding.ItemProfessorAttendanceListBinding
import com.example.goclass.network.dataclass.AttendancesResponse

class ProfessorAttendanceListAdapter(
    private val listener: ProfessorAttendanceListFragment,
) : RecyclerView.Adapter<ProfessorAttendanceListAdapter.ProfessorAttendanceListViewHolder>() {
    private var studentAttendanceList = listOf<AttendancesResponse>()

    // setStudentAttendanceList: Updates the list of student attendance items and notifies the adapter to refresh.
    @SuppressLint("NotifyDataSetChanged")
    fun setStudentAttendanceList(list: List<AttendancesResponse>) {
        studentAttendanceList = list
        notifyDataSetChanged()
    }

    // onCreateViewHolder: Creates ViewHolder instances for professor attendance list items.
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

    // onBindViewHolder: Binds the data to the ViewHolder views based on the position.
    override fun onBindViewHolder(
        holder: ProfessorAttendanceListViewHolder,
        position: Int,
    ) {
        val studentAttendanceItem = studentAttendanceList[position]
        holder.bind(studentAttendanceItem)
    }

    // getItemCount: Returns the total number of professor attendance items.
    override fun getItemCount(): Int = studentAttendanceList.size

    // ProfessorAttendanceListViewHolder: ViewHolder class for professor attendance list items.
    class ProfessorAttendanceListViewHolder(
        val binding: ItemProfessorAttendanceListBinding,
        private val listener: ProfessorAttendanceListFragment,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        // bind: Binds the data to the views and sets up click listeners.
        @SuppressLint("SetTextI18n")
        fun bind(studentAttendanceItem: AttendancesResponse) {
            val attendanceId = studentAttendanceItem.attendanceId
            val studentName = studentAttendanceItem.studentName ?: ""
            val attendanceStatus = studentAttendanceItem.attendanceStatus

            binding.studentNameText.text = studentName

            when (attendanceStatus) {
                2 -> {
                    binding.attendanceStatusText.text = "Present"
                }
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
