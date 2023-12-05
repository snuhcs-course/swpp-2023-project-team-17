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
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorAttendanceListFragment : Fragment() {
    private lateinit var binding: FragmentProfessorAttendanceListBinding
    private val viewModel: ProfessorAttendanceListViewModel by viewModel()
    private lateinit var className: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfessorAttendanceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = classSharedPref!!.getInt("classId", -1)
        className = classSharedPref.getString("className", "")?: ""
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
    }

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
