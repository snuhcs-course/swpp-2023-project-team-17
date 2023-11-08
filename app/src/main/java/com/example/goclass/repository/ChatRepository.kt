package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Messages

class ChatRepository(private val serviceApi: ServiceApi) {
    suspend fun chatChannelGetList(channelId: Int) = serviceApi.chatChannelGetList(channelId)

    suspend fun chatChannelSend(
        channelId: Int,
        messages: Messages
    ) = serviceApi.chatChannelSend(channelId, messages)
}
