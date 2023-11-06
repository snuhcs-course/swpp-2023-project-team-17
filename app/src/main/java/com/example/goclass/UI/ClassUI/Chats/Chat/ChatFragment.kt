package com.example.goclass.UI.ClassUI.Chats.Chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.goclass.R
import com.example.goclass.UI.MainUI.MainActivity
import com.example.goclass.databinding.FragmentChatBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val userSharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userRole = userSharedPref?.getString("userRole", "") ?: ""

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val className = classSharedPref?.getString("className", "") ?: ""
        binding.className.text = className

        // Back Button
        binding.backButton.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        // Attendance Button
        binding.attendanceButton.setOnClickListener {
            when (userRole) {
                "student" -> {
                    findNavController().navigate(R.id.action_chatFragment_to_studentAttendanceFragment)
                }
                "professor" -> {
                    findNavController().navigate(R.id.action_chatFragment_to_professorAttendanceFragment)
                }
            }
        }
    }
}
