package com.example.goclass.main_ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.goclass.R
import com.example.goclass.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val errorTextView = view.findViewById<TextView>(R.id.errorTextView)

        // Logout Button
        binding.logoutButton.setOnClickListener {
            sharedPref ?: return@setOnClickListener
            with(sharedPref.edit()) {
                putBoolean("isLoggedIn", false)
                apply()
            }
            with(sharedPref.edit()) {
                putString("userRole", "")
                apply()
            }
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        // Confirm Button
        val radioGroup = view.findViewById<RadioGroup>(R.id.roleRadioGroup)

        binding.confirmButton.setOnClickListener {
            val selectedRole = when (radioGroup.checkedRadioButtonId) {
                R.id.studentRadioButton -> "student"
                R.id.professorRadioButton -> "professor"
                else -> null
            }

            if (selectedRole == null) {
                errorTextView.visibility = View.VISIBLE
            } else {
                errorTextView.visibility = View.GONE
            }

            selectedRole?.let {
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                with(sharedPref?.edit()) {
                    this?.putString("userRole", it)
                    this?.apply()
                }

                when (it) {
                    "student" -> {
                        findNavController().navigate(R.id.action_profileFragment_to_studentMainFragment)
                    }
                    "professor" -> {
                        findNavController().navigate(R.id.action_profileFragment_to_professorMainFragment)
                    }
                }
            }
        }

        // Remember whether user is a student or a professor
        when (sharedPref?.getString("userRole", "")) {
            "student" -> {
                binding.studentRadioButton.isChecked = true
            }
            "professor" -> {
                binding.professorRadioButton.isChecked = true
            }
            else -> {
                // Do Nothing
            }
        }

        // Keyboard down when you touch other space in screen
        binding.root.setOnTouchListener { _, _ ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
            false
        }
    }
}

