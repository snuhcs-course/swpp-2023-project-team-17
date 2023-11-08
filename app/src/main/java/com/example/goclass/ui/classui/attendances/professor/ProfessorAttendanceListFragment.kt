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
import com.example.goclass.repository.UserRepository
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ProfessorAttendanceListFragment : Fragment() {
    private lateinit var binding: FragmentProfessorAttendanceListBinding
    private val viewModel: ProfessorAttendanceListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfessorAttendanceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val userSharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userSharedPref!!.getInt("userId", -1)
        val classSharedPrf = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val className = classSharedPrf!!.getString("className", "")
        val attendanceSharedPref = activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val date = attendanceSharedPref!!.getString("date", "")!!

        binding.className.text = className
        binding.dateText.text = formatUtcDate(date)

        // Back Button
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_professorAttendanceListFragment_to_professorAttendanceFragment)
        }

        // show professorStudentAttendanceList with dummy data
        val userMap = mapOf("userId" to userId.toString(), "userType" to "1")
        val repository: UserRepository by inject()
        val professorAttendanceListAdapter = ProfessorAttendanceListAdapter(repository)
        binding.professorAttendanceListRecyclerView.adapter = professorAttendanceListAdapter
        binding.professorAttendanceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val studentAttendanceListLiveData = viewModel.getStudentAttendanceList(date, userMap)
        studentAttendanceListLiveData.observe(viewLifecycleOwner) { studentAttendanceList ->
            professorAttendanceListAdapter.setStudentAttendanceList(studentAttendanceList)
        }
    }

    fun formatUtcDate(utcDate: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        originalFormat.timeZone = TimeZone.getTimeZone("UTC") // UTC 시간대 설정
        val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date = originalFormat.parse(utcDate) ?: return ""
        return targetFormat.format(date)
    }
}
