package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class MessagesResponse {
    @SerializedName("messageId")
    int messageId;
    @SerializedName("timeStamp")
    String timeStamp;
    @SerializedName("senderId")
    int senderId;
    @SerializedName("content")
    String content;
    @SerializedName("channelId")
    int channelId;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getMessageId() {
        return messageId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public int getChannelId() {
        return channelId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}