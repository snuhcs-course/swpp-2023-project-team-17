package com.example.goclass.mainUi

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.goclass.R
import com.example.goclass.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            FragmentProfileBinding.inflate(
                inflater,
                container,
                false,
            )
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(
            view,
            savedInstanceState,
        )

        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        saveToSharedPref("userId", 1)

        val storedUserName = sharedPref?.getString("userName", "")
        if (storedUserName != "") {
            binding.nameEditText.setText(storedUserName)
        } else {
            val userId = sharedPref?.getInt("userId", -1) ?: -1
            viewModel.userGet(userId)

            viewModel.userName.observe(
                viewLifecycleOwner,
                Observer { receivedUserName ->
                    binding.nameEditText.setText(receivedUserName)
                    saveToSharedPref("userName", receivedUserName)
                },
            )
        }

        binding = FragmentProfileBinding.bind(view)
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.editSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val selectedRole = when (binding.roleRadioGroup.checkedRadioButtonId)
                    {
                        R.id.studentRadioButton -> "student"
                        R.id.professorRadioButton -> "professor"
                        else -> null
                    }
                saveToSharedPref("userName", binding.nameEditText.text.toString())

                selectedRole?.let {
                    val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    saveToSharedPref("userRole", it)

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
        }

        // Logout Button
        binding.logoutButton.setOnClickListener {
            sharedPref ?: return@setOnClickListener
            saveToSharedPref("isLoggedIn", false)
            saveToSharedPref("userRole", "")
            saveToSharedPref("userName", "")
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        // Confirm Button
        binding.confirmButton.setOnClickListener {
            val userId = sharedPref?.getInt("userId", -1) ?: -1
            val userType =
                when (binding.roleRadioGroup.checkedRadioButtonId) {
                    R.id.studentRadioButton -> 0
                    R.id.professorRadioButton -> 1
                    else -> null
                }
            val userName = binding.nameEditText.text.toString()

            if (userType == null) {
                binding.errorTextView.visibility = View.VISIBLE
            } else if (userName == "") {
                binding.errorNameView.visibility = View.VISIBLE
            } else {
                binding.errorNameView.visibility = View.GONE
                binding.errorTextView.visibility = View.GONE
                viewModel.userEdit(userId, userType, userName)
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

    private fun saveToSharedPref(
        key: String,
        value: Any?,
    ) {
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("Not a valid type to save in Shared Preferences")
            }
            apply()
        }
    }
}
