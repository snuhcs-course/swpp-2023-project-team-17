package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class Comments {
    @SerializedName("messageId")
    int messageId = -1;
    @SerializedName("timeStamp")
    String timeStamp = "";
    @SerializedName("senderId")
    int senderId = -1;
    @SerializedName("content")
    String content = "";
    @SerializedName("channelId")
    int channelId = -1;
    @SerializedName("commentId")
    int commentId = -1;

    public Comments(int messageId, String timeStamp, int senderId, String content, int channelId) {
        this.messageId = messageId;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.content = content;
        this.channelId = channelId;
    }

    public Comments(int senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getSenderId() {
        return senderId;
    }
}
