package com.example.goclass.ui.classui.chats.chat

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.R
import com.example.goclass.databinding.ItemCommentBinding
import com.example.goclass.network.dataclass.Comments

class CommentAdapter(
    private val context: Context,
    private val userId: Int,
    private val onCommentEdit: (Int, String, Int, Int) -> Unit
):
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
        return CommentViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int,
    ) {
        holder.bind(commentList[position], userId) { classId, content, commentId, messageId ->
            onCommentEdit(classId, content, commentId, messageId)
        }
    }

    override fun getItemCount(): Int = commentList.size

    class CommentViewHolder(var binding: ItemCommentBinding, var context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comments, userId: Int, onCommentEdit: (Int, String, Int, Int) -> Unit) {
            binding.commentText.text = comment.content
            binding.commentEditButton.visibility = if (comment.senderId == userId) View.VISIBLE else View.GONE
            binding.nameText.text = comment.senderName

            // Chat Edit Button
            binding.commentEditButton.setOnClickListener {
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.dialog_message_edit)
                dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

                val editText = dialog.findViewById<EditText>(R.id.messageEditText)
                val editButtonDialog = dialog.findViewById<Button>(R.id.messageEditButton)

                editText.setText(comment.content)

                editButtonDialog.setOnClickListener {
                    val content = editText.text.toString()
                    onCommentEdit(comment.classId, content, comment.commentId, comment.messageId)
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
    }
}