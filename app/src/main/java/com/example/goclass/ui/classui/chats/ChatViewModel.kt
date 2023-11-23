package com.example.goclass.ui.classui.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.CommentsResponse
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.network.dataclass.MessagesResponse
import com.example.goclass.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository,
) : ViewModel() {
    private val messageListLiveData : MutableLiveData<List<MessagesResponse>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()
    private val _sendSuccess = MutableLiveData<Boolean>()
    private val _editSuccess = MutableLiveData<Boolean>()

    private val toastMessage: LiveData<String> get() = _toastMessage
    private val sendSuccess: LiveData<Boolean> get() = _sendSuccess
    private val editSuccess: LiveData<Boolean> get() = _editSuccess

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
                    _toastMessage.postValue("Success")
                    _sendSuccess.postValue(true)
                } else {
                    _toastMessage.postValue("Failure")
                    _sendSuccess.postValue(false)
                }
            } catch (e:Exception) {
                Log.d("chatSendError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
                _sendSuccess.postValue(false)
            }
        }
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
                    _toastMessage.postValue("Success")
                    _editSuccess.postValue(true)
                } else {
                    _toastMessage.postValue("Failure")
                    _editSuccess.postValue(false)
                }
            } catch (e:Exception) {
                Log.d("chatEditError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
                _editSuccess.postValue(false)
            }
        }
    }

    fun chatChannelGetList(
        classId: Int,
    ): MutableLiveData<List<MessagesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.chatChannelGetList(classId)
                if(response.code == 200){
                    messageListLiveData.postValue(response.messageList)
                    _toastMessage.postValue("Success")
                } else {
                    _toastMessage.postValue("Failure")
                }
            } catch (e:Exception) {
                Log.d("chatListGetError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return messageListLiveData
    }

    fun accessMessageListLiveData(): MutableLiveData<List<MessagesResponse>> {
        return messageListLiveData
    }

    fun accessToastMessage(): LiveData<String> {
        return toastMessage
    }

    fun accessSendSuccess(): LiveData<Boolean> {
        return sendSuccess
    }

    fun accessEditSuccess(): LiveData<Boolean> {
        return editSuccess
    }
}
