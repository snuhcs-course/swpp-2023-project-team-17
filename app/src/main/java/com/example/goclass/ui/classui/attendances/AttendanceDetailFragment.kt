package com.example.goclass.ui.classui.attendances

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val attendanceSharedPref = activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val className = attendanceSharedPref!!.getString("className", "")
        val userRole = attendanceSharedPref.getString("userType", "")
        val studentId = attendanceSharedPref.getInt("studentId", -1)
        val studentName = attendanceSharedPref.getString("studentName", "")
        val date = attendanceSharedPref.getString("date", "")
        val attendanceStatus = attendanceSharedPref.getInt("attendanceStatus", -1)

        binding.className.text = className
        binding.date.text = date
        binding.studentName.text = studentName

        when (attendanceStatus) {
            2 -> {
                binding.status.text = "Present"
            }
            1 -> {
                binding.status.text = "Late"
            }
            else -> {
                binding.status.text = "Absent"
            }
        }

        // Back Button
        binding.backButton.setOnClickListener {
            if (userRole == "student") {
                findNavController().navigate(R.id.action_attendanceDetailFragment_to_studentAttendanceFragment)
            } else {
                findNavController().navigate(R.id.action_attendanceDetailFragment_to_professorAttendanceListFragment)
            }
        }

        val barChart: BarChart = binding.chart

        // 가상의 데이터 생성 (1분 간격으로 60분간의 데이터)
        val entries = ArrayList<BarEntry>()
        for (i in 0 until 60) {
            val value = if (i % 5 == 0) 0f else 1f // 예시 데이터, 1분 간격으로 교실에 있었는 지 여부를 표현
            entries.add(BarEntry(i.toFloat(), value))
        }

        // BarDataSet Configuration
        val barDataSet = BarDataSet(entries, "In Class")
        barDataSet.color = Color.parseColor("#6D9FFF")
        barDataSet.setDrawValues(false)

        // BarData Configuration
        val barData = BarData(barDataSet)
        barData.barWidth = 1.0f

        // BarChart Configuration
        barChart.data = barData
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.xAxis.isEnabled = false
        barChart.legend.textSize = 18f
        barChart.legend.formSize = 14f
        barChart.axisLeft.setDrawLabels(false)
        barChart.axisLeft.axisMaximum = 1.0f
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.isEnabled = false

        // Bar Update
        barChart.invalidate()

    }
}