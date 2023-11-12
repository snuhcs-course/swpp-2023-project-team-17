package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Comments
import com.example.goclass.network.dataclass.Messages
import java.net.IDN

class ChatRepository(private val serviceApi: ServiceApi) {
    suspend fun chatChannelGetList(classId: Int) = serviceApi.chatChannelGetList(classId)

    suspend fun chatChannelSend(
        classId: Int,
        messages: Messages,
    ) = serviceApi.chatChannelSend(classId, messages)

    suspend fun chatChannelEdit(
        classId: Int,
        messages: Messages,
    ) = serviceApi.chatChannelEdit(classId, messages)

    suspend fun chatCommentGetList(
        classId: Int,
        id: Int,
    ) = serviceApi.chatCommentGetList(classId, id)

    suspend fun chatCommentSend(
        classId: Int,
        id: Int,
        comments: Comments,
    ) = serviceApi.chatCommentSend(classId, id, comments)

    suspend fun chatCommentEdit(
        classId: Int,
        id: Int,
        comments: Comments,
    ) = serviceApi.chatCommentEdit(classId, id, comments)
}
