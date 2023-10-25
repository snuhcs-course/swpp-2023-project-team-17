package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Messages {
    @SerializedName("message_id")
    int messageId = -1;
    @SerializedName("time_stamp")
    String timeStamp = "";
    @SerializedName("sender_id")
    int senderId = -1;
    @SerializedName("content")
    String content = "";
    @SerializedName("channel_id")
    int channelId = -1;

    public Messages(int messageId, String timeStamp, int senderId, String content, int channelId) {
        this.messageId = messageId;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.content = content;
        this.channelId = channelId;
    }

    public Messages(int senderId, String content, int channelId) {
        this.senderId = senderId;
        this.content = content;
        this.channelId = channelId;
    }
}