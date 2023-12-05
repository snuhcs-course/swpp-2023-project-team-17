package com.example.goclass.ui.classui.chats

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
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
    private lateinit var userName: String
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChatCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val userSharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = userSharedPref!!.getInt("userId", -1)
        userName = userSharedPref?.getString("userName", "") ?: ""

        // Back Button
        binding.backButton.setOnClickListener {
            findNavController().navigate(ChatCommentFragmentDirections.actionChatCommentFragmentToChatFragment())
        }

        // Socket.io settings
        socket = IO.socket(getString(R.string.base_url))
        socket?.connect()

        // Keyboard down when you touch other space in screen
        binding.toolbar.setOnTouchListener { _, _ ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
            binding.commentText.clearFocus()
            false
        }
        binding.commentRecyclerView.setOnTouchListener { _, _ ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
            binding.commentText.clearFocus()
            false
        }
        binding.chatMessage.setOnTouchListener { _, _ ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
            binding.commentText.clearFocus()
            false
        }
    }

    override fun onResume() {
        super.onResume()

        val args: ChatCommentFragmentArgs by navArgs()
        val messageId = args.messageId
        val content = args.content
        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = classSharedPref!!.getInt("classId", -1)
        val commentListLiveData = viewModel.chatCommentGetList(classId, messageId)

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
            val comment = binding.commentText.text.toString().trim()
            if (comment.isNotBlank()) {
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
        binding.chatMessage.text = content
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