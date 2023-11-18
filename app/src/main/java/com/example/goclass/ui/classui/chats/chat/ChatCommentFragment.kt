package com.example.goclass.ui.classui.chats.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.databinding.FragmentChatCommentBinding
import com.example.goclass.network.dataclass.CommentsResponse
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatCommentFragment : Fragment() {
    private lateinit var binding: FragmentChatCommentBinding
    private val viewModel: ChatCommentViewModel by viewModel()
    private lateinit var socket: Socket

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChatCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val args: ChatCommentFragmentArgs by navArgs()
        val messageId = args.messageId
        val content = args.content

        val userSharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userSharedPref!!.getInt("userId", -1)
        val userName = userSharedPref?.getString("userName", "") ?: ""

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = classSharedPref!!.getInt("classId", -1)

        binding.chatMessage.text = content

        // Back Button
        binding.backButton.setOnClickListener {
            findNavController().navigate(ChatCommentFragmentDirections.actionChatCommentFragmentToChatFragment())
        }

        val commentListLiveData = viewModel.chatCommentGetList(classId, messageId)

        // Socket.io settings
        socket = IO.socket("http://ec2-43-202-167-120.ap-northeast-2.compute.amazonaws.com:3000")
        socket?.connect()

        // join chat room
        val joinData = JSONObject().apply {
            put("class_id", classId)
            put("comment_id", messageId)
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
                val messageResp = CommentsResponse(id, commentId, senderName, content, timeStamp)

                val updatedMessageList = commentListLiveData.value?.toMutableList() ?: mutableListOf()
                updatedMessageList.add(messageResp)
                commentListLiveData.postValue(updatedMessageList)

                binding.commentRecyclerView.scrollToPosition(updatedMessageList.size - 1)
            }
        }

        // Chat Send Button
        binding.commentSendButton.setOnClickListener {
            val comment = binding.commentText.text.toString()
            val sendData = JSONObject().apply {
                put("msg", comment)
                put("class_id", classId)
                put("sender_name", userName)
                put("comment_id", messageId)
                put("time_stamp", getCurrentTime())
            }
            viewModel.chatCommentSend(classId, messageId, userId, comment)
            socket?.emit("chat", sendData)
            binding.commentText.setText("")
        }

        val commentAdapter = CommentAdapter(requireContext(), userId) { classId, content, commentId, messageId ->
            viewModel.chatCommentEdit(classId, content, commentId, messageId)
        }
        binding.commentRecyclerView.adapter = commentAdapter
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentListLiveData.observe(viewLifecycleOwner) {commentList ->
            commentAdapter.setCommentList(commentList)
        }
        binding.commentRecyclerView.scrollToPosition(commentAdapter.itemCount - 1)
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