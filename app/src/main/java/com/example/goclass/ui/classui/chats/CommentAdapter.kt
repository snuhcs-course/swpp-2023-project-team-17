package com.example.goclass.ui.classui.chats

import android.annotation.SuppressLint
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
import com.example.goclass.network.dataclass.CommentsResponse
import java.text.SimpleDateFormat
import java.util.Locale

class CommentAdapter(
    private val context: Context,
    private val userId: Int,
    private val onCommentEdit: (Int, String, Int, Int) -> Unit
):
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var commentList = listOf<CommentsResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setCommentList(list: List<CommentsResponse>) {
        commentList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentViewHolder {
        val binding =
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
        fun bind(comment: CommentsResponse, userId: Int, onCommentEdit: (Int, String, Int, Int) -> Unit) {
            binding.commentText.text = comment.content
            binding.commentEditButton.visibility = if (comment.senderId == userId) View.VISIBLE else View.GONE
            binding.nameText.text = comment.senderName

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            try {
                val parsedDate = inputFormat.parse(comment.timeStamp)
                val formattedDate = parsedDate?.let { outputFormat.format(it) }
                binding.timeStampText.text = formattedDate
            } catch (e: Exception) {
                e.printStackTrace()
                binding.timeStampText.text = comment.timeStamp
            }

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