package com.example.goclass.ui.classui.chats

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.network.dataclass.MessageDummy
import com.example.goclass.databinding.ItemMessageBinding
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.R
import kotlinx.coroutines.launch

class MessageAdapter(
    private val context: Context,
    private val userId: Int,
    private val onMessageClicked: (Messages) -> Unit,
    private val onMessageEdit: (Int, String, Int) -> Unit
) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private var messageList = listOf<Messages>()

    fun setMessageList(list: List<Messages>) {
        messageList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MessageViewHolder {
        var binding =
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

    class MessageViewHolder(var binding: ItemMessageBinding, var context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Messages, userId: Int, onMessageClicked: (Messages) -> Unit, onMessageEdit: (Int, String, Int) -> Unit) {
            binding.messageText.text = message.content
            binding.chatEditButton.visibility = if (message.senderId == userId) View.VISIBLE else View.GONE
            binding.nameText.text = message.senderName

            itemView.setOnClickListener{
                onMessageClicked(message)
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
