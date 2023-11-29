package com.example.goclass.ui.mainui.usermain

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentProfessorMainBinding
import com.example.goclass.ui.classui.ClassActivity
import com.example.goclass.ui.classui.ClassScheduler
import com.example.goclass.ui.mainui.usermain.utils.InputValidnessTest
import com.example.goclass.ui.mainui.usermain.utils.TimeSelectionLayout
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorMainFragment : Fragment() {
    private lateinit var binding: FragmentProfessorMainBinding
    private val viewModel: ProfessorMainViewModel by viewModel()
    private lateinit var classListAdapter: ClassListAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfessorMainBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref!!.getInt("userId", -1)
        val userName = sharedPref.getString("userName", "") ?: ""

        classListAdapter = ClassListAdapter(viewModel, 1)
        binding.professorClassRecyclerView.adapter = classListAdapter
        binding.professorClassRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Name Textview
        binding.name.text = userName

        viewModel.accessSnackbarMessage().observe(viewLifecycleOwner) { message ->
            Snackbar.make(binding.root, message, Toast.LENGTH_SHORT).show()
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

            // Keyboard down when you touch other space in screen
            dialog.findViewById<ConstraintLayout>(R.id.dialog_create).setOnTouchListener { _, _ ->
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(dialog.currentFocus?.windowToken, 0)
                dialog.currentFocus?.clearFocus()
                true
            }

            createButtonDialog.setOnClickListener {
                val enteredClassName = editClassName.text.toString()
                val enteredClassTime = generateClassTimeString(timeSelectionContainer)
                val enteredBuildingNumber = editBuildingNumber.text.toString()
                val enteredRoomNumber = editRoomNumber.text.toString()
                val enteredCode = editCode.text.toString()

                if (!InputValidnessTest.isClassNameValid(enteredClassName)) {
                    Snackbar.make(dialog.findViewById<EditText>(R.id.classNameEdittext), "Please enter class name.", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.parseColor("#FF515C"))
                        .show()
                    return@setOnClickListener
                }

//                if (!InputValidnessTest.isClassTimeValid(timeSelectionContainer)) {
//                    Snackbar.make(dialog.findViewById<LinearLayout>(R.id.classNameEdittext), "Invalid class time", Snackbar.LENGTH_SHORT)
//                        .setBackgroundTint(Color.parseColor("#FF515c"))
//                        .show()
//                    return@setOnClickListener
//                }

                if (!InputValidnessTest.isClassValid(enteredBuildingNumber, enteredRoomNumber)) {
                    Snackbar.make(dialog.findViewById<LinearLayout>(R.id.classNameEdittext), "Please enter building number and room number.", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.parseColor("#FF515c"))
                        .show()
                    return@setOnClickListener
                }

                if (!InputValidnessTest.isClassCodeValid(enteredCode)) {
                    Snackbar.make(dialog.findViewById<LinearLayout>(R.id.classNameEdittext), "Please enter class code", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.parseColor("#FF515c"))
                        .show()
                    return@setOnClickListener
                }

                viewModel.createClass(
                    enteredClassName,
                    enteredCode,
                    userId,
                    enteredClassTime,
                    enteredBuildingNumber,
                    enteredRoomNumber,
                    ClassScheduler(),
                )
                dialog.dismiss()
            }
            dialog.show()
        }

        // Show ClassList
        val userMap = mapOf("userId" to userId.toString(), "userType" to "1")
        val classListLiveData = viewModel.getClassList(userMap)

        classListLiveData.observe(viewLifecycleOwner) { classList ->
            classListAdapter.setClassList(classList)
        }

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
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
