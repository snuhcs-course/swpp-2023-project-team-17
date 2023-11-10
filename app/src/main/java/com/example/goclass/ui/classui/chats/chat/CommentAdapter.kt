package com.example.goclass.ui.classui.chats.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.databinding.ItemCommentBinding
import com.example.goclass.network.dataclass.Comments

class CommentAdapter(private val userId: Int) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
        private var commentList = listOf<Comments>()

        fun setCommentList(list: List<Comments>) {
            commentList = list
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): CommentViewHolder {
            var binding =
                ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CommentViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: CommentViewHolder,
            position: Int,
        ) {
            holder.bind(commentList[position], userId)
        }

        override fun getItemCount(): Int = commentList.size

        class CommentViewHolder(var binding: ItemCommentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(comment: Comments, userId: Int) {
                binding.commentText.text = comment.content
                binding.commentEditButton.visibility = if (comment.senderId == userId) View.VISIBLE else View.GONE
            }
        }
}