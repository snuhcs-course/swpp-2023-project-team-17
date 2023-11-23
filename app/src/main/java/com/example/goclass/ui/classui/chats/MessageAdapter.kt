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
import com.example.goclass.databinding.ItemMessageBinding
import com.example.goclass.R
import com.example.goclass.network.dataclass.MessagesResponse
import java.text.SimpleDateFormat
import java.util.Date
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
        var binding: ItemMessageBinding,
        var context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessagesResponse, userId: Int, onMessageClicked: (MessagesResponse) -> Unit, onMessageEdit: (Int, String, Int) -> Unit) {
            binding.messageText.text = message.content
            binding.chatEditButton.visibility = if (message.senderId == userId) View.VISIBLE else View.GONE
            binding.nameText.text = message.senderName

            itemView.setOnClickListener {
                onMessageClicked(message)
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

            // Chat Edit Button
            binding.chatEditButton.setOnClickListener {
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.dialog_message_edit)
                dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

                val editText = dialog.findViewById<EditText>(R.id.messageEditText)
                val editButtonDialog = dialog.findViewById<Button>(R.id.messageEditButton)

                editText.setText(message.content)

                editButtonDialog.setOnClickListener {
                    val content = editText.text.toString()
                    onMessageEdit(message.classId, content, message.messageId)
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
    }
}
