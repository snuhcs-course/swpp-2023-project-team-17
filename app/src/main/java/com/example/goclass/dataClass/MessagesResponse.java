package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class MessagesResponse {
    @SerializedName("message_id")
    int messageId;
    @SerializedName("time_stamp")
    String timeStamp;
    @SerializedName("sender_id")
    int senderId;
    @SerializedName("content")
    String content;
    @SerializedName("channel_id")
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