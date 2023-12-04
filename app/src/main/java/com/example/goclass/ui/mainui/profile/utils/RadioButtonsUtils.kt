package com.example.goclass.ui.mainui.profile.utils

import com.example.goclass.databinding.FragmentProfileBinding

object RadioButtonsUtils {
    fun getSelectedRole(binding: FragmentProfileBinding): String? {
        return when {
            binding.professorRadioButton.isChecked -> "professor"
            binding.studentRadioButton.isChecked -> "student"
            else -> null
        }
    }

    fun restoreRadioButtonState(binding: FragmentProfileBinding, storedUserRole: String) {
        when (storedUserRole) {
            "student" -> binding.studentRadioButton.isChecked = true
            "professor" -> binding.professorRadioButton.isChecked = true
        }
    }
}