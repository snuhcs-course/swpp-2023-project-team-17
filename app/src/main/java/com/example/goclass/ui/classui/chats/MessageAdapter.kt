package com.example.goclass.ui.classui.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.network.dataclass.MessageDummy
import com.example.goclass.databinding.ItemMessageBinding
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.Messages

class MessageAdapter(
    private val userId: Int,
    private val onMessageClicked: (Messages) -> Unit
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
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int,
    ) {
        holder.bind(messageList[position], userId, onMessageClicked)
    }

    override fun getItemCount(): Int = messageList.size

    class MessageViewHolder(var binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Messages, userId: Int, onMessageClicked: (Messages) -> Unit) {
            binding.messageText.text = message.content
            binding.chatEditButton.visibility = if (message.senderId == userId) View.VISIBLE else View.GONE

            itemView.setOnClickListener{
                onMessageClicked(message)
            }
        }
    }
}
