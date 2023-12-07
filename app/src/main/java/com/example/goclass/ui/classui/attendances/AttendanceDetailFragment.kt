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
import com.example.goclass.R
import com.example.goclass.databinding.FragmentAttendanceDetailBinding
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
        val userRole = attendanceSharedPref.getString("userRole", "")
        val attendanceId = attendanceSharedPref.getInt("attendanceId", -1)
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

        // BarChart entries
        val barChart: BarChart = binding.chart
        val entries = ArrayList<BarEntry>()
        val attendanceDetailListLiveDate = viewModel.getAttendanceDetail(attendanceId)
        attendanceDetailListLiveDate.observe(viewLifecycleOwner) { attendanceDetailList ->
            if(attendanceDetailList.isNotEmpty()) {
                for ((index, detail) in attendanceDetailList.withIndex()) {
                    if (detail == '1') {
                        entries.add(BarEntry(index.toFloat(), 1f))
                    } else {
                        entries.add(BarEntry(index.toFloat(), 0f))
                    }
                }
            }

            // BarDataSet Configuration
            val barDataSet = BarDataSet(entries, "In Class")
            barDataSet.color = Color.parseColor("#000000")
            barDataSet.setDrawValues(false)

            // BarData Configuration
            val barData = BarData(barDataSet)
            barData.barWidth = 2.0f

            // BarChart Configuration
            barChart.data = barData
            barChart.description.isEnabled = false
            barChart.xAxis.isEnabled = true
            barChart.axisLeft.setDrawLabels(false)
            barChart.axisLeft.axisMaximum = 1.0f
            barChart.axisLeft.axisMinimum = 0f
            barChart.axisRight.isEnabled = false

            // Bar Update
            barChart.invalidate()
        }
    }
}