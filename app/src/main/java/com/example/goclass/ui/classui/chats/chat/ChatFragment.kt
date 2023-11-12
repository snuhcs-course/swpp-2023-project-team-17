package com.example.goclass.ui.classui.chats.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentChatBinding
import com.example.goclass.ui.classui.chats.MessageAdapter
import com.example.goclass.ui.mainui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModel()

    private var userId: Int = -1
    private var classId: Int = -1

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
        userId = userSharedPref!!.getInt("userId", -1)

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        classId = classSharedPref!!.getInt("classId", -1)
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

        // Chat Send Button
        binding.chatSendButton.setOnClickListener {
            viewModel.chatChannelSend(classId, userId, binding.chatText.text.toString())
            binding.chatText.setText("")
        }

        val messageListLiveData = viewModel.chatChannelGetList(classId)
        val messageAdapter = MessageAdapter(requireContext(), userId, {message ->
            val action = ChatFragmentDirections.actionChatFragmentToChatCommentFragment(
                messageId = message.messageId,
                content = message.content,
            )
            findNavController().navigate(action)
        },
        { classId, content, messageId ->
            viewModel.chatChannelEdit(classId, content, messageId)
        })
        binding.chatRecyclerView.adapter = messageAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageListLiveData.observe(viewLifecycleOwner) {messageList ->
            messageAdapter.setMessageList(messageList)
        }
    }

    override fun onResume() {
        super.onResume()

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val newClassName = classSharedPref?.getString("className", "") ?: ""
        val newClassId = classSharedPref?.getInt("classId", -1) ?: -1

        val userSharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val newUserId = userSharedPref?.getInt("userId", -1) ?: -1

        if (newUserId != -1 && newUserId != userId) {
            userId = newUserId
        }
        if (newClassId != -1 && newClassId != classId) {
            classId = newClassId

            viewModel.chatChannelGetList(classId).observe(viewLifecycleOwner) { messageList ->
                (binding.chatRecyclerView.adapter as? MessageAdapter)?.setMessageList(messageList)
            }
        }
        if (newClassName.isNotEmpty()) {
            binding.className.text = newClassName
        }
    }
}
