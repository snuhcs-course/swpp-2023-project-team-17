/*
 * StudentMainFragment is a Fragment class responsible for managing the UI and user interactions
 * on the student's main screen. It includes features such as joining classes and displaying the class list.
 */

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
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentStudentMainBinding
import com.example.goclass.ui.classui.ClassActivity
import com.example.goclass.ui.classui.ClassScheduler
import com.example.goclass.ui.mainui.usermain.utils.InputValidnessTest
import com.example.goclass.utility.SnackbarBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class StudentMainFragment : Fragment() {
    private lateinit var binding: FragmentStudentMainBinding
    private val viewModel: StudentMainViewModel by viewModel {
        parametersOf(requireActivity().application)
    }
    private val professorMainViewModel: ProfessorMainViewModel by viewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStudentMainBinding.inflate(inflater, container, false)
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref!!.getInt("userId", -1)
        val userName = sharedPref.getString("userName", "") ?: ""

        viewModel.snackbarMessage.observe(viewLifecycleOwner) { message ->
            Snackbar.make(binding.root, message, Toast.LENGTH_SHORT).show()
        }

        // Name Textview
        binding.name.text = userName

        // Join Button
        binding.joinButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_join)
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

            val editCode = dialog.findViewById<EditText>(R.id.codeEditText)
            val editName = dialog.findViewById<EditText>(R.id.nameEditText)
            val joinButtonDialog = dialog.findViewById<Button>(R.id.joinButton)

            // Keyboard down when you touch other space in screen
            dialog.findViewById<ConstraintLayout>(R.id.dialog_join).setOnTouchListener { _, _ ->
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(dialog.currentFocus?.windowToken, 0)
                dialog.currentFocus?.clearFocus()
                true
            }

            joinButtonDialog.setOnClickListener {
                val enteredCode = editCode.text.toString()
                val enteredName = editName.text.toString()

                if (!InputValidnessTest.isClassNameValid(enteredName)) {
                    Snackbar.make(dialog.findViewById<EditText>(R.id.nameEditText), "Please enter class name.", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.parseColor("#FF515C"))
                        .show()
                    return@setOnClickListener
                }

                if (!InputValidnessTest.isClassCodeValid(enteredCode)) {
                    Snackbar.make(dialog.findViewById<EditText>(R.id.nameEditText), "Please enter class code.", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.parseColor("#FF515C"))
                        .show()
                    return@setOnClickListener
                }

                viewModel.classJoin(userId, enteredName, enteredCode, ClassScheduler())

                dialog.dismiss()
            }

            dialog.show()
        }

        // show classList with dummy data
        val userMap = mapOf("userId" to userId.toString(), "userType" to "0")
        val classListLiveData = viewModel.getClassList(userMap)
        val classListAdapter = ClassListAdapter(professorMainViewModel, 0)
        binding.studentClassRecyclerView.adapter = classListAdapter
        binding.studentClassRecyclerView.layoutManager = LinearLayoutManager(requireContext())

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
            findNavController().navigate(R.id.action_studentMainFragment_to_profileFragment)
        }

        // recyclerView
        binding.studentClassRecyclerView.setOnClickListener {
            val intent = Intent(view.context, ClassActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSnackbar(message: String, colorResId: Int) {
        SnackbarBuilder(binding.root)
            .setMessage(message)
            .setBackgroundColor(colorResId)
            .build()
            .show()
    }
}
