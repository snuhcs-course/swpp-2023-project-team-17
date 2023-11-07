package com.example.goclass.ui.classui.attendances.student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentStudentAttendanceBinding
import com.example.goclass.repository.AttendanceRepository
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentAttendanceFragment : Fragment() {
    private lateinit var binding: FragmentStudentAttendanceBinding
    private val viewModel: StudentAttendanceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStudentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val userSharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userSharedPref!!.getInt("userId", -1)

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val className = classSharedPref!!.getString("className", "")
        val classId = classSharedPref!!.getInt("classId", -1)

        binding.className.text = className

        // Back Button
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_studentAttendanceFragment_to_chatFragment)
        }

        // show studentAttendanceList with dummy data
        val repository: AttendanceRepository by inject()
        val studentAttendanceAdapter = StudentAttendanceAdapter(repository)
        binding.studentAttendanceRecyclerView.adapter = studentAttendanceAdapter
        binding.studentAttendanceRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val studentAttendanceListLiveData = viewModel.getStudentAttendanceList(classId, userId)
        studentAttendanceListLiveData.observe(viewLifecycleOwner) { studentAttendanceList ->
            studentAttendanceAdapter.setStudentAttendanceList(studentAttendanceList)
        }
    }
}
