package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class Comments {
    @SerializedName("messageId")
    int messageId = -1;
    @SerializedName("senderId")
    int senderId = -1;
    @SerializedName("content")
    String content = "";

    public Comments(int senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public Comments(String content, int messageId) {
        this.messageId = messageId;
        this.content = content;
    }
}
