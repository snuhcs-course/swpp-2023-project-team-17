package com.example.goclass.ui.classui.chats.chat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.R
import com.example.goclass.databinding.FragmentChatBinding
import com.example.goclass.databinding.FragmentChatCommentBinding
import com.example.goclass.databinding.ItemCommentBinding
import com.example.goclass.ui.classui.chats.MessageAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChatCommentFragment : Fragment() {
    private lateinit var binding: FragmentChatCommentBinding
    private val viewModel: ChatCommentViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ChatCommentFragmentArgs by navArgs()
        val messageId = args.messageId
        val content = args.content

        val userSharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userSharedPref!!.getInt("userId", -1)

        val classSharedPref = activity?.getSharedPreferences("ClassPrefs", Context.MODE_PRIVATE)
        val classId = classSharedPref!!.getInt("classId", -1)

        binding.chatMessage.text = content

        binding.backButton.setOnClickListener {
            findNavController().navigate(ChatCommentFragmentDirections.actionChatCommentFragmentToChatFragment())
        }

        binding.commentSendButton.setOnClickListener {
            viewModel.chatCommentSend(classId, messageId, userId, binding.commentText.text.toString())
            binding.commentText.setText("")
        }

        val commentListLiveData = viewModel.chatCommentGetList(classId, messageId)
        val commentAdapter = CommentAdapter(userId)
        binding.commentRecyclerView.adapter = commentAdapter
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentListLiveData.observe(viewLifecycleOwner) {commentList ->
            commentAdapter.setCommentList(commentList)
        }
    }
}