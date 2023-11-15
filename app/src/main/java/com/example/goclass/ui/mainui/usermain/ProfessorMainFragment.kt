package com.example.goclass.ui.mainui.usermain

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.ui.classui.ClassActivity
import com.example.goclass.R
import com.example.goclass.databinding.FragmentProfessorMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.goclass.ui.mainui.usermain.ClassListAdapter
import com.example.goclass.ui.mainui.usermain.utils.TimeSelectionLayout

class ProfessorMainFragment : Fragment() {
    private lateinit var binding: FragmentProfessorMainBinding
    private val viewModel: ProfessorMainViewModel by viewModel()
    private lateinit var classListAdapter: ClassListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfessorMainBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref!!.getInt("userId", -1)
        classListAdapter = ClassListAdapter(viewModel, 1)
        binding.professorClassRecyclerView.adapter = classListAdapter
        binding.professorClassRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        binding.createButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_create)
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

            val editClassName = dialog.findViewById<EditText>(R.id.classNameEdittext)
            val timeSelectionContainer = dialog.findViewById<LinearLayout>(R.id.timeSelectionContainer)
            val addTimeButton = dialog.findViewById<ImageButton>(R.id.addTimeButton)
            val editBuildingNumber = dialog.findViewById<EditText>(R.id.buildingNumberEdittext)
            val editRoomNumber = dialog.findViewById<EditText>(R.id.roomNumberEditText)
            val editCode = dialog.findViewById<EditText>(R.id.codeEdittext)
            val createButtonDialog = dialog.findViewById<Button>(R.id.createButton)

            // Initial TimeSelectionLayout
            val initialTimeSelectionLayout = TimeSelectionLayout.create(dialog.context, false)
            timeSelectionContainer.addView(initialTimeSelectionLayout)

            // Add TimeSelectionLayout Button
            addTimeButton.setOnClickListener {
                val newTimeSelectionLayout = TimeSelectionLayout.create(dialog.context)
                timeSelectionContainer.addView(newTimeSelectionLayout)
            }

            createButtonDialog.setOnClickListener {
                val enteredClassName = editClassName.text.toString()
                val enteredClassTime = generateClassTimeString(timeSelectionContainer)
                val enteredBuildingNumber = editBuildingNumber.text.toString()
                val enteredRoomNumber = editRoomNumber.text.toString()
                val enteredCode = editCode.text.toString()

                viewModel.createClass(enteredClassName, enteredCode, userId, enteredClassTime, enteredBuildingNumber, enteredRoomNumber)
                dialog.dismiss()
            }
            dialog.show()
        }

        // show classList with dummy data
        val userMap = mapOf("userId" to userId.toString(), "userType" to "1")
        val classListLiveData = viewModel.getClassList(userMap)

        classListLiveData.observe(viewLifecycleOwner) { classList ->
            classListAdapter.setClassList(classList)
        }

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Profile Button
        binding.profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_professorMainFragment_to_profileFragment)
        }

        // recyclerView
        binding.professorClassRecyclerView.setOnClickListener {
            val intent = Intent(view.context, ClassActivity::class.java)
            startActivity(intent)
        }

        // hide delete button
        binding.root.setOnTouchListener { _, _ ->
            classListAdapter.hideAllDeleteButtons()
            true
        }
    }

    private fun generateClassTimeString(container: LinearLayout):String {
        val classTimes = mutableListOf<String>()

        for (i in 0 until container.childCount) {
            val layout = container.getChildAt(i) as ConstraintLayout
            val dayDropdown = layout.findViewWithTag<Spinner>("dayDropdown")
            val startTimeButton = layout.findViewWithTag<Button>("startTimeButton")
            val endTimeButton = layout.findViewWithTag<Button>("endTimeButton")

            val day = dayDropdown.selectedItem as String
            val startTime = startTimeButton.text.toString()
            val endTime = endTimeButton.text.toString()

            classTimes.add("$day $startTime-$endTime")
        }

        return classTimes.joinToString(", ")
    }
}
