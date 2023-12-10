/*
 * AttendanceDetailFragment is a Fragment responsible for displaying detailed attendance information for a specific student or class.
 * It includes a BarChart to visualize the attendance details.
 *
 * @viewModel: ViewModel associated with the fragment for handling business logic.
 *
 * onCreateView: Inflates the layout for this fragment.
 * onViewCreated: Called when the fragment's view has been created, where it initializes UI components and handles user interactions.
 */

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

    /*
     * onViewCreated is called when the fragment's view has been created.
     * It initializes UI components, retrieves attendance information, and sets up the BarChart.
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve attendance details from SharedPreferences
        val attendanceSharedPref =
            activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val className = attendanceSharedPref!!.getString("className", "")
        val userRole = attendanceSharedPref.getString("userRole", "")
        val attendanceId = attendanceSharedPref.getInt("attendanceId", -1)
        val studentName = attendanceSharedPref.getString("studentName", "")
        val date = attendanceSharedPref.getString("date", "")
        val attendanceStatus = attendanceSharedPref.getInt("attendanceStatus", -1)

        // Set UI components based on retrieved information
        binding.className.text = className
        binding.date.text = date
        binding.studentName.text = studentName

        // Set attendance status text based on status code
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
            // Navigate back to the appropriate destination based on user role
            if (userRole == "student") {
                findNavController().navigate(R.id.action_attendanceDetailFragment_to_studentAttendanceFragment)
            } else {
                findNavController().navigate(R.id.action_attendanceDetailFragment_to_professorAttendanceListFragment)
            }
        }

        // BarChart entries
        val barChart: BarChart = binding.chart
        val entries = ArrayList<BarEntry>()
        val attendanceLiveData = viewModel.getAttendance(attendanceId)
        attendanceLiveData.observe(viewLifecycleOwner) { attendance ->
            val attendanceDetail = attendance.attendanceDetail
            val classLength = attendanceDetail.length
            if (classLength > 1) {
                for ((index, detail) in attendanceDetail.withIndex()) {
                    if (index == 0) {
                        continue
                    }

                    if (detail == '1') {
                        entries.add(BarEntry(index.toFloat(), 0.9f))
                    } else {
                        entries.add(BarEntry(index.toFloat(), 0f))
                    }
                }
                val attendanceDuration = attendance.attendanceDuration * 100
                val durationPercentage = attendanceDuration.div(classLength - 1)
                binding.duration.text = "Attendance Percentage: $durationPercentage%"
            } else {
                binding.duration.text = "0%"
            }

            // BarDataSet Configuration
            val barDataSet = BarDataSet(entries, "In Class")
            barDataSet.color = Color.parseColor("#000000")
            barDataSet.setDrawValues(false)

            // BarData Configuration
            val barData = BarData(barDataSet)
            barData.barWidth = 0.9f

            // BarChart Configuration
            barChart.data = barData
            barChart.description.isEnabled = false
            barChart.xAxis.isEnabled = true
            barChart.xAxis.axisMinimum = 1f
            barChart.axisLeft.setDrawLabels(false)
            barChart.axisLeft.axisMaximum = 1.0f
            barChart.axisLeft.axisMinimum = 0f
            barChart.axisRight.isEnabled = false

            // Invalidate to update the chart
            barChart.invalidate()
        }
    }
}
