package com.example.goclass.ui.classui.attendances

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentAttendanceDetailBinding
import com.example.goclass.databinding.FragmentProfessorAttendanceBinding
import com.example.goclass.ui.classui.attendances.professor.ProfessorAttendanceAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttendanceDetailFragment : Fragment() {
    private lateinit var binding: FragmentAttendanceDetailBinding
    private val viewModel: AttendanceDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAttendanceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val attendanceSharedPref = activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val className = attendanceSharedPref!!.getString("className", "")
        val userType = attendanceSharedPref.getInt("userType", -1)
        val studentId = attendanceSharedPref.getInt("studentId", -1)
        val studentName = attendanceSharedPref.getString("studentName", "")

        binding.className.text = className

        // Back Button
        binding.backButton.setOnClickListener {
            if (userType == 0) {
                findNavController().navigate(R.id.action_attendanceDetailFragment_to_studentAttendanceFragment)
            } else {
                findNavController().navigate(R.id.action_attendanceDetailFragment_to_professorAttendanceListFragment)
            }
        }
    }
}