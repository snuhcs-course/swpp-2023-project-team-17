package com.example.goclass.ui.classui.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Comments
import com.example.goclass.network.dataclass.CommentsResponse
import com.example.goclass.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatCommentViewModel(
    private val repository: ChatRepository
) : ViewModel() {
    val commentListLiveData : MutableLiveData<List<CommentsResponse>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()
    private val _sendSuccess = MutableLiveData<Boolean>()
    private val _editSuccess = MutableLiveData<Boolean>()

    val toastMessage: LiveData<String> get() = _toastMessage
    val sendSuccess: LiveData<Boolean> get() = _sendSuccess
    val editSuccess: LiveData<Boolean> get() = _editSuccess

    fun chatCommentSend(
        classId: Int,
        id: Int,
        userId: Int,
        content: String,
    ) {
        val sendComments = Comments(userId, content)
        viewModelScope.launch {
            try {
                val response = repository.chatCommentSend(classId, id, sendComments)
                if(response.code == 200){
                    chatCommentGetList(classId, id)
                    _toastMessage.postValue("Success")
                    _sendSuccess.postValue(true)
                } else {
                    _toastMessage.postValue("Failure")
                    _sendSuccess.postValue(false)
                }
            } catch (e: Exception) {
                Log.d("commentSendError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
                _sendSuccess.postValue(false)
            }
        }
    }

    fun chatCommentEdit(
        classId: Int,
        content: String,
        commentId: Int,
        messageId: Int,
    ) {
        val comments = Comments(content, messageId)
        viewModelScope.launch {
            try {
                val response = repository.chatCommentEdit(classId, commentId, comments)
                if(response.code == 200){
                    chatCommentGetList(classId, commentId)
                    _toastMessage.postValue("Success")
                    _editSuccess.postValue(true)
                } else {
                    _toastMessage.postValue("Failure")
                    _editSuccess.postValue(false)
                }
            } catch (e: Exception) {
                Log.d("commentEditError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
                _editSuccess.postValue(false)
            }
        }
    }

    fun chatCommentGetList(
        classId: Int,
        commentId: Int,
    ): MutableLiveData<List<CommentsResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.chatCommentGetList(classId, commentId)
                if(response.code == 200){
                    commentListLiveData.postValue(response.commentList)
                    _toastMessage.postValue("Success")
                } else {
                    _toastMessage.postValue("Failure")
                }
            } catch (e:Exception) {
                Log.d("commentListGetError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return commentListLiveData
    }
}