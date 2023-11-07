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
import com.example.goclass.databinding.FragmentProfessorAttendanceBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorAttendanceFragment : Fragment() {
    private lateinit var binding: FragmentProfessorAttendanceBinding
    private val viewModel: ProfessorAttendanceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfessorAttendanceBinding.inflate(inflater, container, false)
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

        binding.className.text = className

        // Back Button
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_professorAttendanceFragment_to_chatFragment)
        }

        // show professorAttendanceList with dummy data
        val userMap = mapOf("userId" to "1", "userType" to "1")
        val professorAttendanceAdapter = ProfessorAttendanceAdapter(this)
        binding.professorAttendanceRecyclerView.adapter = professorAttendanceAdapter
        binding.professorAttendanceRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val professorAttendanceListLiveData = viewModel.getProfessorAttendanceList(userMap)
        professorAttendanceListLiveData.observe(viewLifecycleOwner) { professorAttendanceList ->
            professorAttendanceAdapter.setProfessorAttendanceList(professorAttendanceList)
        }
    }

    fun onItemClicked(date: String) {
        val attendanceSharedPref = activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        with(attendanceSharedPref?.edit()) {
            this?.putString("date", date)
            this?.apply()
        }
        findNavController().navigate(R.id.action_professorAttendanceFragment_to_professorAttendanceListFragment)
    }
}
