package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class Messages {
    @SerializedName("messageId")
    int messageId = -1;
    @SerializedName("senderId")
    int senderId = -1;
    @SerializedName("content")
    String content = "";

    public Messages(int senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public Messages(String content, int messageId) {
        this.content = content;
        this.messageId = messageId;
    }
}