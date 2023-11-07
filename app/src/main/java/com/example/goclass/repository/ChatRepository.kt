package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Messages

class ChatRepository(private val serviceApi: ServiceApi) {
    suspend fun chatChannelGetList(
        classId: Int,
        channelType: Int,
    ) = serviceApi.chatChannelGetList(classId, channelType)

    suspend fun chatChannelSend(
        classId: Int,
        channelType: Int,
        messages: Messages,
    ) = serviceApi.chatChannelSend(classId, channelType, messages)
}
