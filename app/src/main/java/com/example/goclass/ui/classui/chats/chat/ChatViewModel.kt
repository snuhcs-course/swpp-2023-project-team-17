package com.example.goclass.ui.classui.chats.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository,
) : ViewModel() {
    private val messageListLiveData : MutableLiveData<List<Messages>> = MutableLiveData()

    fun chatChannelSend(
        classId: Int,
        senderId: Int,
        content: String,
    ) {
        val sendMessages = Messages(senderId, content)
        viewModelScope.launch {
            try {
                val response = repository.chatChannelSend(classId, sendMessages)
                if(response.code == 200) {
                    chatChannelGetList(classId)
                } else {
                    //toast message
                }
            } catch (e:Exception) {
                Log.d("chatSendError", e.message.toString())
            }
        }
    }

    fun chatChannelGetList(
        classId: Int,
    ): MutableLiveData<List<Messages>> {
        viewModelScope.launch {
            try {
                val response = repository.chatChannelGetList(classId)
                if(response.code == 200){
                    messageListLiveData.postValue(response.messageList)
                } else {
                    //toast message
                }
            } catch (e:Exception) {
                Log.d("chatListGetError", e.message.toString())
            }
        }
        return messageListLiveData
    }

    fun chatChannelEdit(
        classId: Int,
        content: String,
        messageId: Int,
    ) {
        val editMessages = Messages(content, messageId)
        viewModelScope.launch {
            try {
                val response = repository.chatChannelEdit(classId, editMessages)
                if(response.code == 200) {
                    chatChannelGetList(classId)
                } else {
                    //toast message
                }
            } catch (e:Exception) {
                Log.d("chatEditError", e.message.toString())
            }
        }
    }
}
