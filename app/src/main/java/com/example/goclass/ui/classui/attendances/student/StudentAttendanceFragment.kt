/*
 * StudentAttendanceFragment is a Fragment that displays student attendance information for a specific class.
 * It includes functionality to navigate back to the chat fragment, add attendance, and view attendance details.
 * The fragment uses a RecyclerView to display a list of student attendance items.
 * The attendance data is retrieved from the ViewModel and updated in the RecyclerView through the adapter.
 *
 * @binding: Instance of FragmentStudentAttendanceBinding for accessing the UI elements.
 * @viewModel: Instance of StudentAttendanceViewModel for handling business logic related to student attendance.
 * @className: The name of the class for which attendance information is being displayed.
 *
 * onCreateView: Inflates the layout for this fragment.
 * onViewCreated: Initializes UI elements, sets up event listeners, and fetches student attendance data.
 * onItemClicked: Handles item click events in the RecyclerView, navigates to the attendance detail fragment,
 * and stores relevant data in SharedPreferences for later retrieval.
 */

package com.example.goclass.ui.classui.attendances.student

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentStudentAttendanceBinding
import com.example.goclass.repository.AttendanceRepository
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class StudentAttendanceFragment : Fragment() {
    private lateinit var binding: FragmentStudentAttendanceBinding
    private val viewModel: StudentAttendanceViewModel by viewModel()
    private lateinit var className: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStudentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val userSharedPref =
            activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userSharedPref!!.getInt("userId", -1)

        val classSharedPref =
            activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        className = classSharedPref!!.getString("className", "") ?: ""
        val classId = classSharedPref.getInt("classId", -1)

        binding.className.text = className

        // Back Button
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_studentAttendanceFragment_to_chatFragment)
        }

        // show studentAttendanceList with dummy data
        val repository: AttendanceRepository by inject()
        val studentAttendanceAdapter =
            StudentAttendanceAdapter(repository, viewLifecycleOwner, this)
        binding.studentAttendanceRecyclerView.adapter = studentAttendanceAdapter
        binding.studentAttendanceRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        val studentAttendanceListLiveData =
            viewModel.getStudentAttendanceList(classId, userId)
        studentAttendanceListLiveData.observe(viewLifecycleOwner) { studentAttendanceList ->
            studentAttendanceAdapter.setStudentAttendanceList(studentAttendanceList)
        }
    }

    /*
     * Handles item click events in the RecyclerView, navigates to the attendance detail fragment,
     * and stores relevant data in SharedPreferences for later retrieval.
     *
     * @attendanceId: The ID of the selected attendance record.
     * @studentName: The name of the student associated with the attendance record.
     * @date: The date of the attendance record.
     * @attendanceStatus: The status of the attendance (Present, Late, Absent).
     */
    fun onItemClicked(
        attendanceId: Int,
        studentName: String,
        date: String,
        attendanceStatus: Int,
    ) {
        val attendanceSharedPref =
            activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        with(attendanceSharedPref?.edit()) {
            this?.putString("className", className)
            this?.putString("userRole", "student")
            this?.putInt("attendanceId", attendanceId)
            this?.putString("studentName", studentName)
            this?.putString("date", date)
            this?.putInt("attendanceStatus", attendanceStatus)
            this?.apply()
        }
        findNavController().navigate(R.id.action_studentAttendanceFragment_to_attendanceDetailFragment)
    }
}
