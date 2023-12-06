package com.example.goclass.ui.classui.chats

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.R
import com.example.goclass.databinding.ItemMessageBinding
import com.example.goclass.network.dataclass.MessagesResponse
import java.text.SimpleDateFormat
import java.util.Locale

class MessageAdapter(
    private val context: Context,
    private val userId: Int,
    private val onMessageClicked: (MessagesResponse) -> Unit,
    private val onMessageEdit: (Int, String, Int) -> Unit
): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private var messageList = listOf<MessagesResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMessageList(list: List<MessagesResponse>) {
        messageList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MessageViewHolder {
        val binding =
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int,
    ) {
        holder.bind(messageList[position], userId, onMessageClicked) { classId, content, messageId ->
            onMessageEdit(classId, content, messageId)
        }
    }

    override fun getItemCount(): Int = messageList.size

    class MessageViewHolder(
        val binding: ItemMessageBinding,
        val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var isEditing = false
        fun bind(message: MessagesResponse, userId: Int, onMessageClicked: (MessagesResponse) -> Unit, onMessageEdit: (Int, String, Int) -> Unit) {
            binding.messageText.text = message.content
            binding.chatEditButton.visibility = if (message.senderId == userId) View.VISIBLE else View.GONE
            binding.nameText.text = message.senderName

            itemView.setOnClickListener {
                if (!isEditing) {
                    onMessageClicked(message)
                }
            }

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            try {
                val parsedDate = inputFormat.parse(message.timeStamp)
                val formattedDate = parsedDate?.let { outputFormat.format(it) }
                binding.timeStampText.text = formattedDate
            } catch (e: Exception) {
                e.printStackTrace()
                binding.timeStampText.text = message.timeStamp
            }

            var commentCountText = message.commentCount

            // Comment Count
            if (commentCountText == "0" || commentCountText == null) {
                binding.commentCount.visibility = View.GONE
            } else {
                commentCountText += " comments"
                binding.commentCount.text = commentCountText
            }

            // Chat Edit Button
            binding.chatEditButton.setOnClickListener {
                isEditing = true
                binding.messageText.visibility = View.GONE
                binding.messageEditText.apply {
                    visibility = View.VISIBLE
                    setText(binding.messageText.text)
                    requestFocus()
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
                binding.chatEditButton.visibility = View.GONE
                binding.editDoneButton.visibility = View.VISIBLE
                binding.editCancelButton.visibility = View.VISIBLE
            }

            // Done Button
            binding.editDoneButton.setOnClickListener {
                isEditing = false
                val editedContent = binding.messageEditText.text.toString().trim()
                if (editedContent.isNotBlank()) {
                    onMessageEdit(message.classId, editedContent, message.messageId)
                    binding.messageText.apply {
                        text = editedContent
                        visibility = View.VISIBLE
                    }
                    binding.messageEditText.visibility = View.GONE
                    binding.editDoneButton.visibility = View.GONE
                    binding.editCancelButton.visibility = View.GONE
                }
            }

            // Cancel Button
            binding.editCancelButton.setOnClickListener {
                isEditing = false
                binding.messageEditText.visibility = View.GONE
                binding.messageText.visibility = View.VISIBLE
                binding.chatEditButton.visibility = View.VISIBLE
                binding.editDoneButton.visibility = View.GONE
                binding.editCancelButton.visibility = View.GONE

                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.messageEditText.windowToken, 0)
            }
        }
    }
}
