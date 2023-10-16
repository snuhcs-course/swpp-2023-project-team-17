package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Messages {
    @SerializedName("message_id")
    String messageId;
    @SerializedName("time_stamp")
    String timeStamp;
    @SerializedName("sender_id")
    String senderId;
    @SerializedName("content")
    String content;
    @SerializedName("channel_id")
    String channelId;

    public Messages(String messageId, String timeStamp, String senderId, String content, String channelId) {
        this.messageId = messageId;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.content = content;
        this.channelId = channelId;
    }
}