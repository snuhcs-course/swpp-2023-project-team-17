/*
 * ProfessorAttendanceFragment is a Fragment responsible for displaying professor attendance data
 * in the UI. It includes a RecyclerView to show a list of professor attendance entries.
 *
 * @property viewModel: ProfessorAttendanceViewModel for handling data operations and updates.
 *
 * onCreateView: Inflates the layout for the professor attendance fragment.
 * onViewCreated: Sets up UI components, handles user interactions, and observes LiveData for updates.
 * onItemClicked: Navigates to the ProfessorAttendanceListFragment when a professor attendance item is clicked.
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
import com.example.goclass.databinding.FragmentProfessorAttendanceBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorAttendanceFragment : Fragment() {
    private lateinit var binding: FragmentProfessorAttendanceBinding
    private val viewModel: ProfessorAttendanceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfessorAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val className = classSharedPref!!.getString("className", "")
        val classId = classSharedPref.getInt("classId", -1)

        binding.className.text = className

        // Back Button
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_professorAttendanceFragment_to_chatFragment)
        }

        // show professorAttendanceList with dummy data
        val classMap = mapOf("classId" to classId.toString(), "userType" to "1")
        val professorAttendanceAdapter = ProfessorAttendanceAdapter(this)
        binding.professorAttendanceRecyclerView.adapter = professorAttendanceAdapter
        binding.professorAttendanceRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val professorAttendanceListLiveData = viewModel.getProfessorAttendanceList(classMap)
        professorAttendanceListLiveData.observe(viewLifecycleOwner) { professorAttendanceList ->
            professorAttendanceAdapter.setProfessorAttendanceList(professorAttendanceList)
        }
    }

    // onItemClicked: Navigates to the ProfessorAttendanceListFragment when a professor attendance item is clicked.
    fun onItemClicked(date: String) {
        val attendanceSharedPref = activity?.getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        with(attendanceSharedPref?.edit()) {
            this?.putString("date", date)
            this?.apply()
        }
        findNavController().navigate(R.id.action_professorAttendanceFragment_to_professorAttendanceListFragment)
    }
}
