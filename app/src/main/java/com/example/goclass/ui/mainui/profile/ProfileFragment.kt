package com.example.goclass.ui.mainui.profile

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
import com.example.goclass.ui.mainui.profile.utils.RadioButtonsUtils
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
        val storedUserName = sharedPref?.getString("userName", "") ?: ""
        val storedUserRole = sharedPref?.getString("userRole", "") ?: ""

        if (storedUserName.isNotEmpty()) {
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

        RadioButtonsUtils.restoreRadioButtonState(binding, storedUserRole)

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.editSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val selectedRole = RadioButtonsUtils.getSelectedRole(binding)
                selectedRole?.let {
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

        binding.professorRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.studentRadioButton.isChecked = false
            }
        }

        binding.studentRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.professorRadioButton.isChecked = false
            }
        }

        // Logout Button
        binding.logoutButton.setOnClickListener {
            sharedPref ?: return@setOnClickListener
            saveToSharedPref("isLoggedIn", false)
            saveToSharedPref("userRole", "")
            saveToSharedPref("userName", "")
            saveToSharedPref("userId", -1)
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        // Confirm Button
        binding.confirmButton.setOnClickListener {
            val selectedRole = RadioButtonsUtils.getSelectedRole(binding)
            val userId = sharedPref?.getInt("userId", -1) ?: -1
            val userType =
                when (selectedRole) {
                    "student" -> 0
                    "professor" -> 1
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
