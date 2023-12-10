/*
 * ProfessorAttendanceListFragment is a Fragment that displays the professor's attendance list for a specific date.
 * It utilizes the ProfessorAttendanceListAdapter to populate the RecyclerView with student attendance data.
 *
 * @property viewModel: ProfessorAttendanceListViewModel, the associated ViewModel for handling data and business logic.
 * @property className: String, the name of the class for which the attendance is being displayed.
 *
 * onCreateView: Inflates the layout for the fragment.
 * onViewCreated: Handles the UI setup, initializes the RecyclerView, and observes LiveData for student attendance data.
 * refreshData: Refreshes the student attendance data for the current date and class.
 * onItemClicked: Handles item click events, updating shared preferences and navigating to the attendance detail fragment.
 */

package com.example.goclass.ui.classui.attendances.professor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentProfessorAttendanceListBinding
import com.example.goclass.utility.SnackbarBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorAttendanceListFragment : Fragment() {
    private lateinit var binding: FragmentProfessorAttendanceListBinding
    private val viewModel: ProfessorAttendanceListViewModel by viewModel()
    private lateinit var className: String

    // onCreateView: Inflates the layout for the fragment.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfessorAttendanceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    // onViewCreated: Handles the UI setup, initializes the RecyclerView, and observes LiveData for student attendance data.
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = classSharedPref!!.getInt("classId", -1)
        className = classSharedPref.getString("className", "") ?: ""
        val attendanceSharedPref = activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val date = attendanceSharedPref!!.getString("date", "") ?: ""

        binding.className.text = className
        binding.dateText.text = date

        // Back Button
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_professorAttendanceListFragment_to_professorAttendanceFragment)
        }

        val classMap = mapOf("classId" to classId.toString(), "userType" to "1")
        val professorAttendanceListAdapter = ProfessorAttendanceListAdapter(this)
        binding.professorAttendanceListRecyclerView.adapter = professorAttendanceListAdapter
        binding.professorAttendanceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val studentAttendanceListLiveData = viewModel.getStudentAttendanceList(date, classMap)
        studentAttendanceListLiveData.observe(viewLifecycleOwner) { studentAttendanceList ->
            professorAttendanceListAdapter.setStudentAttendanceList(studentAttendanceList)
        }

        // Refresh Button
        binding.refreshButton.setOnClickListener {
            refreshData()
        }
    }

    // refreshData: Refreshes the student attendance data for the current date and class.
    private fun refreshData() {
        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = classSharedPref!!.getInt("classId", -1)
        val attendanceSharedPref = activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val date = attendanceSharedPref!!.getString("date", "") ?: ""

        val studentAttendanceListLiveData =
            viewModel.getStudentAttendanceList(date, mapOf("classId" to classId.toString(), "userType" to "1"))
        studentAttendanceListLiveData.observe(viewLifecycleOwner) { studentAttendanceList ->
            (binding.professorAttendanceListRecyclerView.adapter as? ProfessorAttendanceListAdapter)
                ?.setStudentAttendanceList(studentAttendanceList)
        }
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            SnackbarBuilder(binding.root)
                .setMessage(message)
                .build()
                .show()
        }
    }

    // onItemClicked: Handles item click events, updating shared preferences and navigating to the attendance detail fragment.
    fun onItemClicked(
        attendanceId: Int,
        studentName: String,
        attendanceStatus: Int,
    ) {
        val attendanceSharedPref = activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        with(attendanceSharedPref?.edit()) {
            this?.putString("className", className)
            this?.putString("userRole", "professor")
            this?.putInt("attendanceId", attendanceId)
            this?.putString("studentName", studentName)
            this?.putInt("attendanceStatus", attendanceStatus)
            this?.apply()
        }
        findNavController().navigate(R.id.action_professorAttendanceListFragment_to_attendanceDetailFragment)
    }
}
