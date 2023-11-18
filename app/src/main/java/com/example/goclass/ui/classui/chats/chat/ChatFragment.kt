package com.example.goclass.ui.classui.chats.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentChatBinding
import com.example.goclass.network.dataclass.MessagesResponse
import com.example.goclass.ui.classui.chats.MessageAdapter
import com.example.goclass.ui.mainui.MainActivity
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModel()

    private var userId: Int = -1
    private var classId: Int = -1
    private lateinit var socket: Socket

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
        val userName = userSharedPref?.getString("userName", "") ?: ""
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

        val messageListLiveData = viewModel.chatChannelGetList(classId)

        // Socket.io settings
        socket = IO.socket("http://ec2-43-202-167-120.ap-northeast-2.compute.amazonaws.com:3000")
        socket?.connect()

        // join chat room
        val joinData = JSONObject().apply {
            put("class_id", classId)
            put("comment_id", -1)
        }
        socket?.emit("joinRoom", joinData)

        // 'chat' Event
        socket?.on("chat") { args ->
            activity?.runOnUiThread {
                val data = args[0] as JSONObject
                val id = data.getInt("class_id")
                val commentId = data.getInt("comment_id")
                val senderName = data.getString("sender_name")
                val content = data.getString("msg")
                val timeStamp = data.getString("time_stamp")
                val messageResp = MessagesResponse(id, commentId, senderName, content, timeStamp)
                val updatedMessageList = messageListLiveData.value?.toMutableList() ?: mutableListOf()
                updatedMessageList.add(messageResp)
                messageListLiveData.postValue(updatedMessageList)
                binding.chatRecyclerView.scrollToPosition(updatedMessageList.size - 1)
            }
        }

        // Chat Send Button
        binding.chatSendButton.setOnClickListener {
            val message = binding.chatText.text.toString()
            val sendData = JSONObject().apply {
                put("msg", message)
                put("class_id", classId)
                put("sender_name", userName)
                put("comment_id", -1)
                put("time_stamp", getCurrentTime())
            }
            viewModel.chatChannelSend(classId, userId, message)
            socket?.emit("chat", sendData)
            binding.chatText.setText("")
        }

        val messageAdapter = MessageAdapter(requireContext(), userId, {message ->
            val action = ChatFragmentDirections.actionChatFragmentToChatCommentFragment(
                messageId = message.messageId,
                content = message.content,
            )
            findNavController().navigate(action)
        }, { classId, content, messageId ->
            viewModel.chatChannelEdit(classId, content, messageId)
        })
        binding.chatRecyclerView.adapter = messageAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageListLiveData.observe(viewLifecycleOwner) {messageList ->
            messageAdapter.setMessageList(messageList)
        }
        binding.chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
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

    override fun onPause() {
        super.onPause()
        socket?.disconnect()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        socket?.disconnect()
    }

    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentTime = Date()
        return dateFormat.format(currentTime)
    }
}
