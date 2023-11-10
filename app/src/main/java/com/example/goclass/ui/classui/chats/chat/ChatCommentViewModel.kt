package com.example.goclass.ui.classui.chats.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Comments
import com.example.goclass.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatCommentViewModel(
    private val repository: ChatRepository
) : ViewModel() {
    private val commentListLiveData : MutableLiveData<List<Comments>> = MutableLiveData()

    fun chatCommentSend(
        classId: Int,
        id: Int,
        userId : Int,
        content : String,
    ) {
        val sendComments = Comments(userId, content)
        viewModelScope.launch {
            try {
                val response = repository.chatCommentSend(classId, id, sendComments)
                if(response.code == 200){
                    chatCommentGetList(classId, id)
                } else {
                    //toast
                }
            } catch (e: Exception) {
                Log.d("commentSendError", e.message.toString())
            }
        }
    }

    fun chatCommentGetList(
        classId: Int,
        id: Int,
    ): MutableLiveData<List<Comments>> {
        viewModelScope.launch {
            try {
                val response = repository.chatCommentGetList(classId, id)
                if(response.code == 200){
                    commentListLiveData.postValue(response.commentList)
                } else {
                    //toast message
                }
            } catch (e:Exception) {
                Log.d("commentListGetError", e.message.toString())
            }
        }
        return commentListLiveData
    }
}