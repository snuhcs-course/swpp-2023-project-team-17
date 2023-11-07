package com.example.goclass.ui.mainui.usermain

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.ui.classui.ClassActivity
import com.example.goclass.R
import com.example.goclass.databinding.FragmentProfessorMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorMainFragment : Fragment() {
    private lateinit var binding: FragmentProfessorMainBinding
    private val viewModel: ProfessorMainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfessorMainBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref!!.getInt("userId", -1)

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        binding.createButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_create)
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

            val editCode = dialog.findViewById<EditText>(R.id.codeEdittext)
            val editClassName = dialog.findViewById<EditText>(R.id.classNameEdittext)
            val editClassTime = dialog.findViewById<EditText>(R.id.classTimeEdittext)
            val editBuildingNumber = dialog.findViewById<EditText>(R.id.buildingNumberEdittext)
            val editRoomNumber = dialog.findViewById<EditText>(R.id.roomNumberEditText)
            val createButtonDialog = dialog.findViewById<Button>(R.id.createButton)
            val startTimeButton = dialog.findViewById<Button>(R.id.startTimeButton)
            val endTimeButton = dialog.findViewById<Button>(R.id.endTimeButton)
            val mondayCheckbox = dialog.findViewById<CheckBox>(R.id.mondayCheckbox)
            val tuesdayCheckbox = dialog.findViewById<CheckBox>(R.id.tuesdayCheckbox)
            val wednesdayCheckbox = dialog.findViewById<CheckBox>(R.id.wednesdayCheckbox)
            val thursdayCheckbox = dialog.findViewById<CheckBox>(R.id.thursdayCheckbox)
            val fridayCheckbox = dialog.findViewById<CheckBox>(R.id.fridayCheckbox)

            startTimeButton.setOnClickListener {
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener {
                            _,
                            hourOfDay,
                            minute, ->
                        startTimeButton.text = String.format("%02d:%02d", hourOfDay, minute)
                    }

                val timePickerDialog =
                    TimePickerDialog(
                        requireContext(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        timeSetListener,
                        12,
                        0,
                        true,
                    )

                timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                timePickerDialog.show()
            }

            endTimeButton.setOnClickListener {
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener {
                            _,
                            hourOfDay,
                            minute, ->
                        endTimeButton.text = String.format("%02d:%02d", hourOfDay, minute)
                    }

                val timePickerDialog =
                    TimePickerDialog(
                        requireContext(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        timeSetListener,
                        12,
                        0,
                        true,
                    )

                timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                timePickerDialog.show()
            }


            createButtonDialog.setOnClickListener {
                val enteredCode = editCode.text.toString()
                val enteredClassName = editClassName.text.toString()
                val classTime = "${startTimeButton.text}-${endTimeButton.text}"
                val combinedClassTime =
                    generateClassTimeString(
                        classTime,
                        mondayCheckbox,
                        tuesdayCheckbox,
                        wednesdayCheckbox,
                        thursdayCheckbox,
                        fridayCheckbox,
                    )
                val enteredClassTime = editClassTime.text.toString()
                val finalClassTime =
                    if (enteredClassTime.isNotEmpty()) {
                        "$combinedClassTime, $enteredClassTime"
                    } else {
                        combinedClassTime
                    }
                val enteredBuildingNumber = editBuildingNumber.text.toString()
                val enteredRoomNumber = editRoomNumber.text.toString()

                viewModel.createClass(enteredClassName, enteredCode, userId, finalClassTime, enteredBuildingNumber, enteredRoomNumber)

                dialog.dismiss()
            }
            dialog.show()
        }

        // show classList with dummy data
        val userMap = mapOf("userId" to "1", "userType" to "1")
        val classListLiveData = viewModel.getClassList(userMap)
        val classListAdapter = ClassListAdapter()
        binding.professorClassRecyclerView.adapter = classListAdapter
        binding.professorClassRecyclerView.layoutManager = LinearLayoutManager(requireContext())

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
    }

    private fun generateClassTimeString(
        classTime: String,
        mondayCheckbox: CheckBox,
        tuesdayCheckbox: CheckBox,
        wednesdayCheckbox: CheckBox,
        thursdayCheckbox: CheckBox,
        fridayCheckbox: CheckBox,
    ): String {
        val selectedDays = mutableListOf<String>()

        if (mondayCheckbox.isChecked) {
            selectedDays.add("Mon $classTime")
        }
        if (tuesdayCheckbox.isChecked) {
            selectedDays.add("Tue $classTime")
        }
        if (wednesdayCheckbox.isChecked) {
            selectedDays.add("Wed $classTime")
        }
        if (thursdayCheckbox.isChecked) {
            selectedDays.add("Thu $classTime")
        }
        if (fridayCheckbox.isChecked) {
            selectedDays.add("Fri $classTime")
        }

        return selectedDays.joinToString(", ")
    }
}
